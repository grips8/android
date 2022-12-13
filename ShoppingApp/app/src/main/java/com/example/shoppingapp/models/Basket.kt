package com.example.shoppingapp.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Basket : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var products: RealmList<BasketProduct> = realmListOf()
}