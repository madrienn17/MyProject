package com.example.myproject.api

import com.example.myproject.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestaurantApiService  {

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(
            @Path("id") id : Int
    ): Response<Restaurant>

    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<RestaurantResponse>

    @GET("restaurants")
    suspend fun getRestaurantByPrice(
            @Query("price")price:Int
    ): Response<RestaurantByPrice>

    @GET("restaurants")
    suspend fun getRestaurantsByCity(
        @Query("city")city:String,
        @Query("page")page:Int
        ): Response<RestaurantsByCity>

    @GET("restaurants")
    suspend fun getRestaurantByCountry(
        @Query("country")country:String,
        @Query("page")page:Int
    ): Response<RestaurantsByCountry>

    @GET("cities")
    suspend fun getCities():Response<CityResponse>

    @GET("countries")
    suspend fun getCountries():Response<CountryResponse>
}

