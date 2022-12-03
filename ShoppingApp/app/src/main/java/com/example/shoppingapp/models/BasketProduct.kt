package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class BasketProduct : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var product: Product? = null
    var quantity: Int = 1
}