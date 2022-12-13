package com.example.shoppingapp.services

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.shoppingapp.models.Basket
import com.example.shoppingapp.models.BasketProduct
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.models.Product
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class DBService() : Service() {
    private val binder = LocalBinder()
    private lateinit var realm : Realm
    private var BASKET_ID: String = "6394d7231153c730376aa1bf"
    private val USER_ID: Int = 1

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(RealmConfiguration.Builder(setOf(Basket::class, BasketProduct::class, Category::class, Product::class)).build())
    }

    /** methods for clients  */
    // TODO: setUser that finds user's basket or creates it

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
            basket = copyToRealm(Basket())
            Log.d("basket: id: ", basket!!._id)
            BASKET_ID = basket!!._id
        }
        return basket
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