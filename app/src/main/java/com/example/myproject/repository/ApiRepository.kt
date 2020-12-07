package com.example.myproject.repository

import com.example.myproject.api.RestaurantApiClient
import com.example.myproject.models.*

class ApiRepository {
    suspend fun getRestaurantsByCity(city: String, page: Int): RestaurantsByCity {
        return RestaurantApiClient.api.getRestaurantsByCity(city,page)
    }
    suspend fun getRestaurantsByCountry(country: String, page:Int): RestaurantsByCountry {
        return RestaurantApiClient.api.getRestaurantByCountry(country, page)
    }
    suspend fun getCities(): CityResponse {
        return RestaurantApiClient.api.getCities()
    }
    suspend fun getCountries(): CountryResponse {
        return RestaurantApiClient.api.getCountries()
    }
    suspend fun getRestaurantById(id:Int): Restaurant {
        return RestaurantApiClient.api.getRestaurantById(id)
    }
}