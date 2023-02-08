package com.example.shoppingapp.services

import com.example.shoppingapp.models.*
import com.example.shoppingapp.utils.JsonBasket
import com.example.shoppingapp.utils.JsonOrder
import com.example.shoppingapp.utils.MoshiBasketAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:8080"
private val moshi = Moshi.Builder()
    .add(MoshiBasketAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KtorApiService {
    @GET("products")
    suspend fun getProducts(@Header("firebaseIDToken") firebaseIDToken: String) : List<Product>
    @GET("categories")
    suspend fun getCategories(@Header("firebaseIDToken") firebaseIDToken: String) : List<Category>
    @GET("basket/{user_id}/get")
    suspend fun getBasket(@Path("user_id") uid: String, @Header("firebaseIDToken") firebaseIDToken: String) : List<JsonBasket>
    @POST("basket/{user_id}/post")
    suspend fun postBasket(@Path("user_id") uid: String, @Body basket: Basket, @Header("firebaseIDToken") firebaseIDToken: String)
    @POST("products/post")
    suspend fun postProduct(@Body product: Product, @Header("firebaseIDToken") firebaseIDToken: String)
    @POST("orders/{user_id}/post")
    suspend fun postOrder(@Path("user_id") uid: String, @Body cardOrder: CardOrder, @Header("firebaseIDToken") firebaseIDToken: String)
    @GET("orders/{user_id}/get")
    suspend fun getOrders(@Path("user_id") uid: String, @Header("firebaseIDToken") firebaseIDToken: String) : List<JsonOrder>
    @POST("stripeOrders/{user_id}/create-payment-intent")
    suspend fun postStripeIntent(@Path("user_id") uid: String, @Header("firebaseIDToken") firebaseIDToken: String, @Body order: Order) : String
    @POST("notifications/say-hi")
    suspend fun postHiToServer() : String
}

object KtorApi {
    val retrofitService: KtorApiService by lazy {
        retrofit.create(KtorApiService::class.java)
    }
}