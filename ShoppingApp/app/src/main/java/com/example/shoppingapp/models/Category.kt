package com.example.shoppingapp.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Category : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId.create().toString()
    var name: String = ""
}