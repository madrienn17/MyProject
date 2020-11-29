package com.example.myproject.api

import com.example.myproject.models.CityResponse
import com.example.myproject.models.CountryResponse
import com.example.myproject.models.RestaurantResponse
import com.example.myproject.models.RestaurantsByCity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RestaurantApiService  {

    @GET("stats")
    fun getStats():
            Call<ArrayList<String>>

    @GET("restaurants")
    suspend fun getRestaurantsByCity(
        @Query("city")city:String
        ): RestaurantsByCity

    @GET("restaurants")
    suspend fun getRestaurantByCountry(
        @Query("state")state:String,
        @Query("page")page:Int
    ): RestaurantResponse

    @GET("cities")
    suspend fun getCities(): CityResponse

    @GET("countries")
    suspend fun getCountries(): CountryResponse
}

