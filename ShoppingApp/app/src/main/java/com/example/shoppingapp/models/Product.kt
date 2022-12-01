package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Product : RealmObject, java.io.Serializable {
    @PrimaryKey
    @Transient
    var _id: ObjectId = ObjectId.create()
    var name: String = ""
    var price: Double = 0.0
    var description: String = ""
    var category: Category? = null
}