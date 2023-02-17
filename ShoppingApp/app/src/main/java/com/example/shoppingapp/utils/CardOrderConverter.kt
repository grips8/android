package com.example.shoppingapp.utils

import android.util.Log
import com.example.shoppingapp.models.*

class CardOrderConverter {
    fun validateCardDetails(_name: String, _number: String, _exp: String, _cvv: String) : CardDetails {
        val name = _name.trim()
        val number = _number.filterNot { it.isWhitespace() }
        val exp = _exp.filterNot { it.isWhitespace() }
        val cvv = _cvv.filterNot { it.isWhitespace() }

        if (name == "")
            throw CardOrderException("Name cannot be empty!")

        val numberRegex = Regex("^\\d{16}\$")
        if (!number.matches(numberRegex))
            throw CardOrderException("Invalid card number!")

        val expRegex = Regex("^((0[1-9])|(1[012]))/2[3-9]\$")
        if (!exp.matches(expRegex))
            throw CardOrderException("Invalid expiration date!")

        val cvvRegex = Regex("^\\d{3}$")
        if (!cvv.matches(cvvRegex))
            throw CardOrderException("Invalid cvv number!")

        return CardDetails(name, number, exp, cvv)
    }

    fun validateProduct(basketProduct: BasketProduct) : JsonBasketProduct {
        if (basketProduct._id.isEmpty())
            throw CardOrderException("Error, basketProduct id is empty!")
        if (basketProduct.quantity < 1)
            throw CardOrderException("Error, basketProduct quantity is less than 1!")
        if (basketProduct.product == null)
            throw CardOrderException("Error, there is no product attached to the basketProduct!")
        if (basketProduct.product!!._id.isEmpty())
            throw CardOrderException("Error, product id is empty!")
        return JsonBasketProduct(basketProduct._id, basketProduct.product!!._id, basketProduct.quantity)
    }

    fun validateBasket(basket: Basket) : JsonBasket {
        if (basket._id.isEmpty())
            throw CardOrderException("Error, basket id is empty!")
        if (basket.products.isEmpty())
            throw CardOrderException("Error, basket is empty!")

        val jsonProductList: MutableList<JsonBasketProduct> = mutableListOf()
        basket.products.forEach { basketProduct ->
            try {
                jsonProductList.add(validateProduct(basketProduct))
            }
            catch (e: CardOrderException) {
                e.message?.let { Log.e("Product validation exception: ", it) }
                throw CardOrderException("Basket product is corrupted!")
            }
        }

        return JsonBasket(basket._id, jsonProductList)
    }
}