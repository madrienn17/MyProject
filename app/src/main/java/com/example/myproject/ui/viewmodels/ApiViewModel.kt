package com.example.myproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myproject.models.*
import com.example.myproject.repository.ApiRepository
import retrofit2.Response


class ApiViewModel(private val repository: ApiRepository) : ViewModel() {
    suspend fun getRestaurantsByCity(city: String, page: Int): Response<RestaurantsByCity> {
       return repository.getRestaurantsByCity(city,page)
    }

    suspend fun getRestaurantsByCountry(country:String, page:Int): Response<RestaurantsByCountry> {
        return  repository.getRestaurantsByCountry(country, page)
    }

    suspend fun getCities(): Response<CityResponse> {
        return repository.getCities()
    }

    suspend fun getCountries(): Response<CountryResponse> {
        return repository.getCountries()
    }

    suspend fun getRestaurantsById(id:Long): Response<Restaurant> {
      return  repository.getRestaurantById(id)
    }

    suspend fun getAllRestaurants(): Response<RestaurantResponse> {
        return repository.getAllRestaurants()
    }
    suspend fun getRestaurantsByAll(price: Int?, city: String?, country: String?): Response<RestaurantResponse> {
        return repository.getRestaurantsByAll(price,city,country)
    }
}