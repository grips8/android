package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Order : RealmObject {
    @PrimaryKey
    var _id = ObjectId.create().toString()
    var basket: Basket? = null
}
