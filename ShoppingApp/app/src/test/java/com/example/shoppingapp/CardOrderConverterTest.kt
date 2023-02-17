package com.example.shoppingapp

import com.example.shoppingapp.models.*
import com.example.shoppingapp.utils.CardOrderConverter
import com.example.shoppingapp.utils.CardOrderException
import com.example.shoppingapp.utils.JsonBasket
import com.example.shoppingapp.utils.JsonBasketProduct
import io.realm.kotlin.ext.realmListOf
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class CardOrderConverterTest {
    private val converter = CardOrderConverter()

    private val empty = ""
    private val validName = "John Doe "
    private val invalidNumber1 = "1234 1234 1234 1234 1"
    private val invalidNumber2 = ""
    private val invalidNumber3 = "1234 123d 1234 1234"
    private val validNumber1 = "1204 1234 1294 1234"
    private val validNumber2 = "  0234 12  34 123 4 1 234  "
    private val invalidExp1 = "00/24"
    private val invalidExp2 = "10/22"
    private val invalidExp3 = "13/24"
    private val invalidExp4 = "1/24"
    private val invalidExp5 = "10/40"
    private val validExp = "12/24"
    private val invalidCvv = "1d23"
    private val validCvv = "091"

    private lateinit var validCategory: Category
    private lateinit var validProduct: Product
    private lateinit var emptyIdProduct: Product
    private lateinit var validBasketProduct: BasketProduct
    private lateinit var nullProductBasketProduct: BasketProduct
    private lateinit var invalidQuantityBasketProduct: BasketProduct
    private lateinit var emptyIdBasketProduct: BasketProduct
    private lateinit var emptyIdBasket: Basket
    private lateinit var emptyBasket: Basket
    private lateinit var basketWithInvalidProduct: Basket
    private lateinit var validBasket: Basket

    @Before
    fun init() {
        validCategory = Category().apply {
            name = "Food"
        }
        validProduct = Product().apply {
            name = "avocado"
            price = 4.20
            description = "lorem ipsum dolor sit amet"
            category = validCategory
        }
        emptyIdProduct = Product().apply {
            _id = ""
            name = "avocado"
            price = 4.20
            description = "lorem ipsum dolor sit amet"
            category = validCategory
        }
        validBasketProduct = BasketProduct().apply {
            product = validProduct
            quantity = 13
        }
        nullProductBasketProduct = BasketProduct().apply {
            product = null
            quantity = 13
        }
        invalidQuantityBasketProduct = BasketProduct().apply {
            product = validProduct
            quantity = 0
        }
        emptyIdBasketProduct = BasketProduct().apply {
            _id = ""
            product = validProduct
            quantity = 13
        }
        emptyIdBasket = Basket().apply {
            _id = ""
            products = realmListOf(validBasketProduct)
        }
        emptyBasket = Basket().apply {
            products = realmListOf()
        }
        basketWithInvalidProduct = Basket().apply {
            products = realmListOf(validBasketProduct, emptyIdBasketProduct, validBasketProduct)
        }
        validBasket = Basket().apply {
            products = realmListOf(validBasketProduct, validBasketProduct, validBasketProduct)
        }

    }

    @Test
    fun detailsEmptyName() {
        try {
            converter.validateCardDetails(empty, validNumber1, validExp, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Name cannot be empty!", e.message)
        }
    }

    @Test
    fun invalidNumber() {
        try {
            converter.validateCardDetails(validName, invalidNumber1, validExp, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid card number!", e.message)
        }
        try {
            converter.validateCardDetails(validName, invalidNumber2, validExp, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid card number!", e.message)
        }
        try {
            converter.validateCardDetails(validName, invalidNumber3, validExp, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid card number!", e.message)
        }
    }

    @Test
    fun invalidExp() {
        try {
            converter.validateCardDetails(validName, validNumber1, invalidExp1, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid expiration date!", e.message)
        }
        try {
            converter.validateCardDetails(validName, validNumber1, invalidExp2, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid expiration date!", e.message)
        }
        try {
            converter.validateCardDetails(validName, validNumber1, invalidExp3, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid expiration date!", e.message)
        }
        try {
            converter.validateCardDetails(validName, validNumber1, invalidExp4, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid expiration date!", e.message)
        }
        try {
            converter.validateCardDetails(validName, validNumber1, invalidExp5, validCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid expiration date!", e.message)
        }
    }

    @Test
    fun invalidCvv() {
        try {
            converter.validateCardDetails(validName, validNumber1, validExp, invalidCvv)
        }
        catch (e: CardOrderException) {
            assertEquals("Invalid cvv number!", e.message)
        }
    }

    @Test
    fun validDetails() {
        val cardDetails1 = converter.validateCardDetails(validName, validNumber1, validExp, validCvv)
        assertThat(cardDetails1, instanceOf(CardDetails::class.java))
        val cardDetails2 = converter.validateCardDetails(validName, validNumber2, validExp, validCvv)
        assertThat(cardDetails2, instanceOf(CardDetails::class.java))
    }

    @Test
    fun validProduct() {
        val basketProduct = converter.validateProduct(validBasketProduct)
        assertThat(basketProduct, instanceOf(JsonBasketProduct::class.java))
    }

    @Test
    fun invalidProduct() {
        try {
            converter.validateProduct(BasketProduct().apply {
                product = emptyIdProduct
                quantity = 13
            })
        }
        catch (e: CardOrderException) {
            assertEquals("Error, product id is empty!", e.message)
        }
        try {
            converter.validateProduct(nullProductBasketProduct)
        }
        catch (e: CardOrderException) {
            assertEquals("Error, there is no product attached to the basketProduct!", e.message)
        }
        try {
            converter.validateProduct(invalidQuantityBasketProduct)
        }
        catch (e: CardOrderException) {
            assertEquals("Error, basketProduct quantity is less than 1!", e.message)
        }
        try {
            converter.validateProduct(emptyIdBasketProduct)
        }
        catch (e: CardOrderException) {
            assertEquals("Error, basketProduct id is empty!", e.message)
        }
    }

    @Test
    fun validBasket() {
        val basket = converter.validateBasket(validBasket)
        assertThat(basket, instanceOf(JsonBasket::class.java))
    }

    @Test
    fun invalidBasket() {
        try {
            converter.validateBasket(emptyIdBasket)
        } catch (e: CardOrderException) {
            assertEquals("Error, basket id is empty!", e.message)
        }
        try {
            converter.validateBasket(emptyBasket)
        } catch (e: CardOrderException) {
            assertEquals("Error, basket is empty!", e.message)
        }
        try {
            converter.validateBasket(basketWithInvalidProduct)
        } catch (e: CardOrderException) {
            assertEquals("Basket product is corrupted!", e.message)
        }
    }

}