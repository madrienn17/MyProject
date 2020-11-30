package com.example.myproject.repository

import com.example.myproject.api.RestaurantApiClient
import com.example.myproject.models.CityResponse
import com.example.myproject.models.RestaurantsByCity

class ApiRepository {
    suspend fun getRestaurantsByCity(city: String): RestaurantsByCity{
        return RestaurantApiClient.api.getRestaurantsByCity(city)
    }
    suspend fun getCities(): CityResponse{
        return RestaurantApiClient.api.getCities()
    }
}