package com.example.shoppingapp.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Basket : RealmObject {
    @PrimaryKey
    val _id: ObjectId = ObjectId.create()
    val products: RealmList<Product> = realmListOf()
}