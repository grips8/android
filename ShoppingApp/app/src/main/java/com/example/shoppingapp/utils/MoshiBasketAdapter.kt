package com.example.shoppingapp.utils

import com.example.shoppingapp.models.Basket
import com.example.shoppingapp.models.BasketProduct
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

class MoshiBasketAdapter {
    @ToJson fun toJson(realmList: RealmList<BasketProduct>) : List<BasketProduct> {
        val mutableList: MutableList<BasketProduct> = mutableListOf<BasketProduct>().apply {
            this.addAll(realmList)
        }
        return mutableList.toList()
    }
    @ToJson fun toJson(basketProduct: BasketProduct) : JsonBasketProduct {
        return JsonBasketProduct(basketProduct._id, basketProduct.product!!._id, basketProduct.quantity)
    }
    @FromJson fun fromJson(list: List<BasketProduct>) : RealmList<BasketProduct> {
        val realmList: RealmList<BasketProduct> = realmListOf<BasketProduct>().apply {
            this.addAll(list)
        }
        return realmList
    }
}