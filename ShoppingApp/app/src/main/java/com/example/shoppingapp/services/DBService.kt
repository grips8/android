package com.example.shoppingapp.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.util.TimeUtils
import com.example.shoppingapp.models.*
import com.google.firebase.auth.FirebaseAuth
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DBService() : Service() {
    private val binder = LocalBinder()
    private lateinit var realm : Realm
    private var BASKET_ID: String = ""
    private var USER_ID: String = ""
    private var allowedUsers: List<String> = listOf("anTAvEP3nWX9qdT9hY1cdJAvsyE2")
    var productsChanged = false
    var currentIdToken = ""
    var tokenIssuedAt = 0L

    override fun onCreate() {
        super.onCreate()
        FirebaseAuth.getInstance().currentUser?.let { mUser ->
            USER_ID = mUser.uid
        }
//        getUserToken()

        runBlocking {
            val fetchedProducts = async { getProductsFromApi() }
            val fetchedCategories = async { getCategoriesFromApi() }
            realm = Realm.open(RealmConfiguration.Builder(setOf(Basket::class, BasketProduct::class, Category::class, Product::class, User::class)).build())
            deleteAllProducts()
            deleteAllCategories()
            runBlocking {
                fetchedCategories.await()?.forEach {
                    launch { upsertCategory(it) }
                }
            }
            runBlocking {
                fetchedProducts.await()?.forEach {
                    launch { upsertProduct(it) }        // should be swapped for normal insert? (no need to check if item is present in realm)
                }
            }
            // not sure if runBlocking here are necessary
            deleteBaskets()
            deleteBasketProducts()
            getBasketFromApi(USER_ID)?.let {
                insertBasket(it)
                BASKET_ID = it._id
                return@let
            } ?: run {
                Basket().also {
                    insertBasket(it)
                    BASKET_ID = it._id
                }
            }
        }
    }

    /** network methods */
    private suspend fun getBasketFromApi(uid: String) : Basket? {
        try {
            var basket: Basket? = null
            val arr = KtorApi.retrofitService.getBasket(uid, getUserToken())
            if (arr.isNotEmpty())
                basket = Basket(arr[0], realm)
            return basket
        }
        catch (e: Exception) {
            Log.e("network: ", "Failed to load data (basket) from network!")
            Log.e("network: ", e.message.toString())
        }
        return null
    }

    private suspend fun getProductsFromApi() : List<Product>? {
        try {
            return KtorApi.retrofitService.getProducts(getUserToken())
        }
        catch (e: Exception) {
            Log.e("network: ", "Failed to load (products) data from network!")
            Log.e("network: ", e.message.toString())
        }
        return null
    }

    private suspend fun getCategoriesFromApi() : List<Category>? {
        try {
            return KtorApi.retrofitService.getCategories(getUserToken())
        }
        catch (e: Exception) {
            Log.e("network: ", "Failed to load data (categories) from network!")
            Log.e("network: ", e.message.toString())
        }
        return null
    }

    private suspend fun postBasketToApi(basket: Basket) {
        try {
            KtorApi.retrofitService.postBasket(USER_ID, basket, getUserToken())
        }
        catch (e: Exception) {
            Log.e("network: ", "Failed to save data (basket) to network server!")
            Log.e("network: ", e.message.toString())
        }
    }

    private suspend fun postProductToApi(product: Product) {
        try {
            KtorApi.retrofitService.postProduct(product, getUserToken())
        }
        catch (e: Exception) {
            Log.e("network: ", "Failed to save data (product) to network server!")
            Log.e("network: ", e.message.toString())
        }
    }

    /** helper methods */
    private fun getUserToken() : String {
        val time = System.currentTimeMillis() / 1000
        if (time - tokenIssuedAt > 3000) {      // token refreshes every 1hr
            FirebaseAuth.getInstance().currentUser?.let { mUser ->
                runBlocking {
                    mUser.getIdToken(true).await().apply {
                        tokenIssuedAt = this.issuedAtTimestamp
                        this.token?.let { token ->
                            currentIdToken = token
                        }
                    }
                }
            }
        }
        return currentIdToken
    }

    private fun getBasket() : Basket? {
        return realm.query<Basket>("_id = '${BASKET_ID}'").first().find()
    }

    private fun insertBasket(basket: Basket) {
        realm.writeBlocking {
            this.copyToRealm(basket.apply {
                this.products.replaceAll {
                    copyToRealm(it.apply {
                        this.product = findLatest(this.product!!)
                    })
                }
            })
        }
    }
    suspend fun upsertProduct(product: Product) {
        realm.write {
            if (realm.query<Product>("_id = '${product._id}'").first().find() === null) {
                val cat: Category? = realm.query<Category>("_id = '${product.category!!._id}'").first().find()
                if (cat !== null)
                    product.category = findLatest(cat)
                else
                    product.category = null
                this.copyToRealm(product)
            }
        }
    }

    private suspend fun upsertCategory(category: Category) {
        realm.write {
            if (realm.query<Category>("_id = '${category._id}'").first().find() === null) {
                this.copyToRealm(category)
            }
        }
    }

    private fun deleteAllProducts() {
        realm.writeBlocking {
            val products: RealmResults<Product> = this.query<Product>().find()
            delete(products)
        }
    }

    private fun deleteAllCategories() {
        realm.writeBlocking {
            val categories: RealmResults<Category> = this.query<Category>().find()
            delete(categories)
        }
    }

    private fun deleteAllUsers() {
        realm.writeBlocking {
            val users: RealmResults<User> = this.query<User>().find()
            delete(users)
        }
    }

    private fun deleteBaskets() {
        realm.writeBlocking {
            val baskets: RealmResults<Basket> = this.query<Basket>().find()
            delete(baskets)
        }
    }

    private fun deleteBasketProducts() {
        realm.writeBlocking {
            val basketProducts: RealmResults<BasketProduct> = this.query<BasketProduct>().find()
            delete(basketProducts)
        }
    }

    /** methods for clients  */
    fun postProduct(product: Product) {
        runBlocking {
            postProductToApi(product)
            getProductsFromApi()?.forEach {
                launch { upsertProduct(it) }        // should be swapped for normal insert? (no need to check if item is present in realm)
            }
        }
    }

    fun isUserAllowed() : Boolean {
        return allowedUsers.contains(USER_ID)
    }

    fun getAllProducts() : MutableList<Product> {
        val res: RealmResults<Product> = realm.query<Product>().find()
        val products: MutableList<Product> = mutableListOf()
        products.addAll(res)
        return products
    }

    fun getAllCategories() : MutableList<Category> {
        val res: RealmResults<Category> = realm.query<Category>().find()
        val categories: MutableList<Category> = mutableListOf()
        categories.addAll(res)
        return categories
    }

    fun getAllBasketProducts() : MutableList<BasketProduct> {
        val basket: Basket? = getBasket()
        if (basket !== null) {
            val products: MutableList<BasketProduct> = mutableListOf()
            products.addAll(basket.products)
            return products
        }
        return mutableListOf()
    }

    fun checkIfProductIsInBasket(product: Product) : BasketProduct? {
        val basket: Basket? = getBasket()
        if (basket !== null) {
            basket.products.forEach {
                if (it.product!!._id == product._id)
                    return it
            }
        }
        return null
    }

    // links basket to product, because no such products are in the basket
    fun addFirstProductToBasket(product: Product) {
        val basket: Basket? = getBasket()
        if (basket !== null) {
            realm.writeBlocking {
                val basketProduct = copyToRealm(BasketProduct().apply {
                    this.product = findLatest(product)
                    quantity = 1
                })
                findLatest(basket)?.products?.add(basketProduct)
            }
        }
    }

    // only changes quantity of product in basket, because one or more of them are present in the basket
    fun addLatterProductToBasket(basketProduct: BasketProduct) : BasketProduct {
        realm.writeBlocking {
            findLatest(basketProduct)?.apply { quantity += 1 }
        }
//        CoroutineScope(Dispatchers.Main).launch {
//            val flow: Flow<ResultsChange<BasketProduct>> = realm.query<BasketProduct>("_id = '${basketProduct._id}'").asFlow()
//            flow.take(1).collect {
//                basketProduct.quantity = it.list[0].quantity
//            }
//        }
            // it should be done using LiveData, but I don't have time to learn it (it seems difficult)
            // meanwhile this workaround will have to do
        return realm.query<BasketProduct>("_id = '${basketProduct._id}'").first().find()!!
    }

    // removes exactly one product from basket (changes quantity if possible)
    fun removeProductFromBasket(basketProduct: BasketProduct) : BasketProduct? {
        if (basketProduct.quantity > 1) {
            realm.writeBlocking {
                findLatest(basketProduct)?.apply { quantity -= 1 }
            }
            // again, it should be done using LiveData
            return realm.query<BasketProduct>("_id = '${basketProduct._id}'").first().find()
        }
        else {
            val basket: Basket? = getBasket()
            if (basket !== null) {
                CoroutineScope(Dispatchers.Main).launch {
                    realm.writeBlocking {
                        findLatest(basket)?.products?.remove(findLatest(basketProduct))
                        findLatest(basketProduct)?.also { delete(it) }
                    }
                }
            }
            return null
        }
    }

    fun initDBWithExampleData() {
        realm.writeBlocking {
            val cat = this.copyToRealm(Category().apply {
                _id = "639b4e7e376bd42ef6caa5f3"
                name = "Food"
            })
            this.copyToRealm(Product().apply {
                name = "Avocado"
                price = 7.99
                description = "cool avocado"
                category = cat
            })
            this.copyToRealm(Product().apply {
                name = "Bread"
                price = 4.20
                description = "cool bread"
                category = null
            })
        }
    }
    fun initDBBasket() : Basket? {
        var basket: Basket? = null
        realm.writeBlocking {
            basket = copyToRealm(Basket().apply {
                _id = "639b4e7e376bd42ef6caa5fc"
            })
            Log.d("basket: id: ", basket!!._id)
            BASKET_ID = basket!!._id
        }
        return basket
    }
    fun initDBUser(basket: Basket?) {
        realm.writeBlocking {
            val user = this.copyToRealm(User().apply {
                _id = "639b4e7e376bd42ef6caa5fa"
                login = "student"
                password = "root"
                firstName = "Ewan"
                surname = "McGregor"
                this.basket = basket
            })
            Log.d("user: id: ", user._id)
            USER_ID = user._id
        }
    }
    /** end of client methods **/

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): DBService = this@DBService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        runBlocking {
            postBasketToApi(getBasket()!!)
        }
        realm.close()
        Log.i("DBService", "><><><><><><><><DESTROYING><><><><><><><><><")
        super.onDestroy()
    }
}