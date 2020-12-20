package com.example.myproject.api

import com.example.myproject.models.CityResponse
import com.example.myproject.models.CountryResponse
import com.example.myproject.models.Restaurant
import com.example.myproject.models.RestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestaurantApiService  {

    //get single restaurant by id
    @GET("restaurants/{id}")
    suspend fun getRestaurantById(
            @Path("id") id : Long
    ): Response<Restaurant>

    // get list with all restaurants
    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<RestaurantResponse>

    // get restaurants by any parameters given, all of them being optional
    @GET("restaurants")
    suspend fun getRestaurantByPriceCityCountry(
            @Query("price") price:Int?,
            @Query("city") city:String?,
            @Query("country") country: String?,
            @Query("page")page: Int?
    ): Response<RestaurantResponse>

    // get the list of cities
    @GET("cities")
    suspend fun getCities():Response<CityResponse>

    //get the list of countries
    @GET("countries")
    suspend fun getCountries():Response<CountryResponse>
}

