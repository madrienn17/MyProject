package com.example.myproject.repository

import com.example.myproject.api.RestaurantApiClient
import com.example.myproject.models.CityResponse
import com.example.myproject.models.CountryResponse
import com.example.myproject.models.RestaurantsByCity
import com.example.myproject.models.RestaurantsByCountry

class ApiRepository {
    suspend fun getRestaurantsByCity(city: String): RestaurantsByCity {
        return RestaurantApiClient.api.getRestaurantsByCity(city)
    }
    suspend fun getRestaurantsByCountry(country: String): RestaurantsByCountry {
        return RestaurantApiClient.api.getRestaurantByCountry(country)
    }
    suspend fun getCities(): CityResponse {
        return RestaurantApiClient.api.getCities()
    }
    suspend fun getCountries(): CountryResponse {
        return RestaurantApiClient.api.getCountries()
    }
}