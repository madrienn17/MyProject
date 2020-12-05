package com.example.myproject.api

import com.example.myproject.models.*
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
        @Query("state")state:String
    ): RestaurantsByCountry

    @GET("cities")
    suspend fun getCities(): CityResponse

    @GET("countries")
    suspend fun getCountries(): CountryResponse
}

