package com.example.myproject.repository

import com.example.myproject.api.RestaurantApiClient
import com.example.myproject.models.*
import retrofit2.Response

class ApiRepository {
    suspend fun getRestaurantsByCity(city: String, page: Int): Response<RestaurantsByCity> {
        return RestaurantApiClient.api.getRestaurantsByCity(city,page)
    }
    suspend fun getRestaurantsByCountry(country: String, page:Int): Response<RestaurantsByCountry> {
        return RestaurantApiClient.api.getRestaurantByCountry(country, page)
    }
    suspend fun getCities(): Response<CityResponse> {
        return RestaurantApiClient.api.getCities()
    }
    suspend fun getCountries(): Response<CountryResponse> {
        return RestaurantApiClient.api.getCountries()
    }
    suspend fun getRestaurantById(id:Int):Response<Restaurant> {
        return RestaurantApiClient.api.getRestaurantById(id)
    }
    suspend fun getRestaurantByPrice(price:Int):Response<RestaurantByPrice>{
        return RestaurantApiClient.api.getRestaurantByPrice(price)
    }
    suspend fun getAllRestaurants():Response<RestaurantResponse> {
        return RestaurantApiClient.api.getAllRestaurants()
    }
    suspend fun getRestaurantsByAll(price: Int?, city: String?, country: String?):Response<RestaurantResponse> {
        return RestaurantApiClient.api.getRestaurantByPriceCityCountry(price,city,country)
    }
}