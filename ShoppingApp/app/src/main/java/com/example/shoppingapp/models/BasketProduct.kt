package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class BasketProduct : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var product: Product? = null
    var quantity: Int = 1
}