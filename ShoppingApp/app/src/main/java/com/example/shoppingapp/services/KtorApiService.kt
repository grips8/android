package com.example.shoppingapp.services

import com.example.shoppingapp.models.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://10.0.2.2:8080"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KtorApiService {
    @GET("products")
    suspend fun getProducts() : List<Product>
}

object KtorApi {
    val retrofitService: KtorApiService by lazy {
        retrofit.create(KtorApiService::class.java)
    }
}