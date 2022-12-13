package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Product : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var name: String = ""
    var price: Double = 0.0
    var description: String = ""
    var category: Category? = null
}