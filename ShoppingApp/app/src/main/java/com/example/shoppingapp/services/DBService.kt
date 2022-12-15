package com.example.shoppingapp.services

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.shoppingapp.models.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlin.coroutines.EmptyCoroutineContext

class DBService() : Service() {
    private val binder = LocalBinder()
    private lateinit var realm : Realm
    private var BASKET_ID: String = "639b4e7e376bd42ef6caa5fa"
    private var USER_ID: String = "639b4e7e376bd42ef6caa5fc"

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            val result = async { getProductsFromApi() }
            realm = Realm.open(RealmConfiguration.Builder(setOf(Basket::class, BasketProduct::class, Category::class, Product::class, User::class)).build())
            deleteAllProducts()
            result.await()?.forEach {
                println(">>>>>> ${it._id}, ${it.name}, ${it.price}")
                launch { upsertProduct(it) }
            }
        }
    }

    /** network methods */
    private suspend fun getProductsFromApi() : List<Product>? {
        try {
            return KtorApi.retrofitService.getProducts()
        }
        catch (e: Exception) {
            Log.d("network: ", "Failed to load data from network!")
        }
        return null
    }

    private suspend fun upsertProduct(product: Product) {
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

    private fun deleteAllProducts() {
        realm.writeBlocking {
            val products: RealmResults<Product> = this.query<Product>().find()
            delete(products)
        }
    }

    /** methods for clients  */
    fun getAllProducts() : MutableList<Product> {
        val res: RealmResults<Product> = realm.query<Product>().find()
        val products: MutableList<Product> = mutableListOf()
        products.addAll(res)
        return products
    }

    fun getAllBasketProducts() : MutableList<BasketProduct> {
        val basket: Basket? = realm.query<Basket>("_id = '$BASKET_ID'").first().find()
        if (basket !== null) {
            val products: MutableList<BasketProduct> = mutableListOf()
            products.addAll(basket.products)
            return products
        }
        return mutableListOf()
    }

    fun checkIfProductIsInBasket(product: Product) : BasketProduct? {
        val basket: Basket? = realm.query<Basket>("_id = '$BASKET_ID'").first().find()
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
        val basket: Basket? = realm.query<Basket>("_id = '$BASKET_ID'").first().find()
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
            val basket: Basket? = realm.query<Basket>("_id = '$BASKET_ID'").first().find()
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
        realm.close()
        super.onDestroy()
    }
}