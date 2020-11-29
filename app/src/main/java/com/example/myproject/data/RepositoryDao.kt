package com.example.myproject.data

import androidx.lifecycle.LiveData
import com.example.myproject.models.Restaurant

class RepositoryDao(private val restaurant: RestaurantDao) {
    val getAll: LiveData<List<Restaurant>> = restaurant.selectAllRestaurants()

    suspend fun addRestaurantDao(restaurant: Restaurant) {
        this.restaurant.insertRestaurantDao(restaurant)
    }

    suspend fun deleteRestaurantDao(restaurant: Restaurant) {
        this.restaurant.deleteRestaurantDao(restaurant)
    }

    suspend fun clearAll() {
        this.restaurant.deleteAllDao()
    }
}