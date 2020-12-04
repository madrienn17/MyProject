package com.example.myproject.repository

import androidx.lifecycle.LiveData
import com.example.myproject.data.RestaurantDao
import com.example.myproject.models.Favorite

class RepositoryDao(private val restaurant: RestaurantDao) {
    val getAll: LiveData<List<Favorite>> = restaurant.selectAllRestaurants()

    suspend fun addRestaurantDao(favorite: Favorite) {
        this.restaurant.insertRestaurantDao(favorite)
    }

    suspend fun deleteRestaurantDao(favorite: Favorite) {
        this.restaurant.deleteRestaurantDao(favorite)
    }

    suspend fun clearAll() {
        this.restaurant.deleteAllDao()
    }
}