package com.example.myproject.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.data.RestaurantDao
import com.example.myproject.models.Favorite

class RepositoryDao(private val restaurant: RestaurantDao) {
    val getAll: LiveData<List<Favorite>> = restaurant.selectAllRestaurants()

    suspend fun insert(favorite: Favorite) {
        restaurant.insert(favorite)
    }

    suspend fun deleteRestaurantDao(favorite: Favorite) {
        restaurant.deleteRestaurantDao(favorite)
    }

    suspend fun clearAll() {
        restaurant.deleteAllDao()
    }

    suspend fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int {
        return restaurant.vacuumDb(supportSQLiteQuery)
    }
}