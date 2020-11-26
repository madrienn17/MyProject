package com.example.myproject.api

import com.example.myproject.models.RestaurantsByCity

class RestaurantApiRepository {
    suspend fun getRestaurantsByCity(city: String): RestaurantsByCity{
        return RestaurantApiClient.api.getRestaurantsByCity(city)
    }
}