package com.example.myproject.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.models.*
import com.example.myproject.repository.ApiRepository


class ApiViewModel(private val repository: ApiRepository) : ViewModel() {
    val restaurantsByCity: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val restaurantsByCountry: MutableLiveData<List<Restaurant>> = MutableLiveData()

    private suspend fun getRestaurantsByCity(city: String, page: Int) {
        val res = repository.getRestaurantsByCity(city, page)
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

    suspend fun loadRestaurantsByCity(city: String, page:Int) {
        getRestaurantsByCity(city, page)
    }

    private suspend fun getRestaurantsByCountry(country:String?, page:Int) {
        val res = country?.let { repository.getRestaurantsByCountry(it, page) }
        restaurantsByCountry.value = res?.let { restaurantsByCountryConverter(it) }
    }

    suspend fun loadRestaurantsByCountry(country: String, page:Int) {
        getRestaurantsByCountry(country, page)
    }

    private fun restaurantsByCountryConverter(restaurantsByCountry: RestaurantsByCountry?): List<Restaurant> {
        val list = mutableListOf<Restaurant>()

        for (i in restaurantsByCountry?.restaurants!!) {
            val restaurant = Restaurant(
                    i.id,
                    i.name,
                    i.address,
                    i.city,
                    i?.state,
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

    suspend fun getRestaurantsById(id:Int): Restaurant {
      return  repository.getRestaurantById(id)
    }

    private fun countriesConverter(country: CountryResponse) : List<String> {
        val countryResponse = mutableListOf<String>()

        for (i in country.countries) {
            countryResponse.add(i)
        }
        return countryResponse
    }
}