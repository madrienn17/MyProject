package com.example.myproject.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.models.CityResponse
import com.example.myproject.models.Restaurant
import com.example.myproject.models.RestaurantsByCity
import com.example.myproject.repository.ApiRepository


class ApiViewModel(private val repository: ApiRepository) : ViewModel() {
    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val cities: MutableLiveData<List<String>> = MutableLiveData()

    suspend fun getRestaurantsByCity(city: String) {
        val res = repository.getRestaurantsByCity(city)
        restaurants.value = restaurantByCityConverter(res)
    }

    private fun restaurantByCityConverter(restaurantsByCity: RestaurantsByCity): List<Restaurant> {
        val list = mutableListOf<Restaurant>()

        for(i in restaurantsByCity.restaurants){
            val restaurant = Restaurant (
                    i.id,
                    i.name,
                    i.address,
                    i.city,
                    i.state,
                    i.area,
                    i.postal_code,
                    i.country,
                    i.phone,
                    i.lat,
                    i.lng,
                    i.price,
                    i.reserve_url,
                    i.mobile_reserve_url,
                    i.image_url
            )
            list.add(restaurant)
        }
        return list
    }

    suspend fun loadRestaurants(city: String) {
        getRestaurantsByCity(city)
    }

    suspend fun getCities() {
        val res = repository.getCities()
        cities.value = citiesConverter(res)
    }

    private fun citiesConverter(city: CityResponse): List<String> {
        val cityresponse = mutableListOf<String>()

        for (i in city.cities) {
            cityresponse.add(i)
        }
        return cityresponse
    }
}