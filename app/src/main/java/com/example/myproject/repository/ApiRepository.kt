package com.example.myproject.repository

import com.example.myproject.api.RestaurantApiClient
import com.example.myproject.models.CityResponse
import com.example.myproject.models.CountryResponse
import com.example.myproject.models.Restaurant
import com.example.myproject.models.RestaurantResponse
import retrofit2.Response

class ApiRepository {

    suspend fun getCities(): Response<CityResponse> {
        return RestaurantApiClient.api.getCities()
    }
    suspend fun getCountries(): Response<CountryResponse> {
        return RestaurantApiClient.api.getCountries()
    }
    suspend fun getRestaurantById(id:Long):Response<Restaurant> {
        return RestaurantApiClient.api.getRestaurantById(id)
    }
    suspend fun getAllRestaurants():Response<RestaurantResponse> {
        return RestaurantApiClient.api.getAllRestaurants()
    }
    suspend fun getRestaurantsByAll(price: Int?, city: String?, country: String?, page:Int?):Response<RestaurantResponse> {
        return RestaurantApiClient.api.getRestaurantByPriceCityCountry(price,city,country,page)
    }
}