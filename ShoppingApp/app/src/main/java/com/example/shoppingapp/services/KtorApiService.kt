package com.example.shoppingapp.services

import com.example.shoppingapp.models.Basket
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.JsonBasket
import com.example.shoppingapp.utils.MoshiBasketAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://10.0.2.2:8080"
private val moshi = Moshi.Builder()
    .add(MoshiBasketAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KtorApiService {
    @GET("products")
    suspend fun getProducts() : List<Product>
    @GET("categories")
    suspend fun getCategories() : List<Category>
    @GET("basket/{user_id}/get")
    suspend fun getBasket(@Path("user_id") uid: String) : List<JsonBasket>
    @POST("basket/{user_id}/post")
    suspend fun postBasket(@Path("user_id") uid: String, @Body basket: Basket)
    @POST("products/post")
    suspend fun postProduct(@Body product: Product)
}

object KtorApi {
    val retrofitService: KtorApiService by lazy {
        retrofit.create(KtorApiService::class.java)
    }
}