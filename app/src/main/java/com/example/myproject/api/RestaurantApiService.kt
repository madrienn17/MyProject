package com.example.myproject.api

import com.example.myproject.models.RestaurantsByCity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RestaurantApiService  {

    @GET("stats")
    fun getStats():
            Call<ArrayList<String>>

    @GET("restaurants")
    suspend fun getRestaurantsByCity(@Query("city")city:String): RestaurantsByCity
}

