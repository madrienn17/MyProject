package com.example.myproject.api

import com.example.myproject.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestaurantApiClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
    val api by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

}