package com.example.shoppingapp.models

import com.example.shoppingapp.utils.JsonBasket
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Basket() : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var products: RealmList<BasketProduct> = realmListOf()

    constructor(jsonBasket: JsonBasket, realm: Realm) : this() {
        _id = jsonBasket._id
        jsonBasket.products.forEach {
            products.add(BasketProduct().apply {
                this._id = it._id
                this.quantity = it.quantity
                this.product = realm.query<Product>("_id = '${it.p_id}'").first().find()
            })
        }
    }
}