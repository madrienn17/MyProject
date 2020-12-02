package com.example.myproject.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.models.*
import com.example.myproject.repository.ApiRepository


class ApiViewModel(private val repository: ApiRepository) : ViewModel() {
    val restaurantsByCity: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val restaurantsByCountry: MutableLiveData<List<Restaurant>> = MutableLiveData()

    private suspend fun getRestaurantsByCity(city: String) {
        val res = repository.getRestaurantsByCity(city)
        restaurantsByCity.value = restaurantByCityConverter(res)
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

    suspend fun loadRestaurantsByCity(city: String) {
        getRestaurantsByCity(city)
    }

    private suspend fun getRestaurantsByCountry(country:String) {
        val res = repository.getRestaurantsByCountry(country)
        restaurantsByCountry.value = restaurantsByCountryConverter(res)
    }

    suspend fun loadRestaurantsByCountry(country: String) {
        getRestaurantsByCountry(country)
    }

    private fun restaurantsByCountryConverter(restaurantsByCountry: RestaurantsByCountry): List<Restaurant> {
        val list = mutableListOf<Restaurant>()

        for (i in restaurantsByCountry.restaurants) {
            val restaurant = Restaurant(
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

    suspend fun getCities(): List<String> {
        val res = repository.getCities()
        return citiesConverter(res)
    }

    private fun citiesConverter(city: CityResponse): List<String> {
        val cityResponse = mutableListOf<String>()

        for (i in city.cities) {
            cityResponse.add(i)
        }
        return cityResponse
    }

    suspend fun getCountries(): List<String> {
        val res = repository.getCountries()
        return countriesConverter(res)
    }

    private fun countriesConverter(country: CountryResponse) : List<String> {
        val countryResponse = mutableListOf<String>()

        for (i in country.countries) {
            countryResponse.add(i)
        }
        return countryResponse
    }
}