package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class User : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var login: String = ""
    var password: String = ""
    var firstName: String = ""
    var surname: String = ""
    var basket: Basket? = null
}