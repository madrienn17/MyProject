package com.example.myproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myproject.models.CityResponse
import com.example.myproject.models.CountryResponse
import com.example.myproject.models.Restaurant
import com.example.myproject.models.RestaurantResponse
import com.example.myproject.repository.ApiRepository
import retrofit2.Response


class ApiViewModel(private val repository: ApiRepository) : ViewModel() {

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
    suspend fun getRestaurantsByAll(price: Int?, city: String?, country: String?, page: Int?): Response<RestaurantResponse> {
        return repository.getRestaurantsByAll(price,city,country, page)
    }
}