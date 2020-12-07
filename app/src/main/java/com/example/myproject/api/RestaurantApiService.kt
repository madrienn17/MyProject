package com.example.myproject.api

import com.example.myproject.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestaurantApiService  {

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(
            @Path("id") id : Int
    ): Restaurant


    @GET("restaurants")
    suspend fun getRestaurantsByCity(
        @Query("city")city:String,
        @Query("page")page:Int
        ): RestaurantsByCity

    @GET("restaurants")
    suspend fun getRestaurantByCountry(
        @Query("country")country:String,
        @Query("page")page:Int
    ): RestaurantsByCountry

    @GET("cities")
    suspend fun getCities(): CityResponse

    @GET("countries")
    suspend fun getCountries(): CountryResponse
}

