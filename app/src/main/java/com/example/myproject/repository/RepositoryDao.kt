package com.example.myproject.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.data.RestaurantDao
import com.example.myproject.models.Favorite
import com.example.myproject.models.User

class RepositoryDao(private val restaurant: RestaurantDao) {
    val getAll: LiveData<List<Favorite>> = restaurant.selectAllRestaurants()
    val getAllUsers: LiveData<List<User>> = restaurant.selectAllUsers()

    suspend fun insert(favorite: Favorite) {
        restaurant.insert(favorite)
    }

    suspend fun deleteRestaurantDao(favorite: Favorite) {
        restaurant.deleteRestaurantDao(favorite)
    }

    suspend fun clearAll() {
        restaurant.deleteAllFavorites()
    }

    suspend fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int {
        return restaurant.vacuumDb(supportSQLiteQuery)
    }

    suspend fun insertUser(user:User) {
        restaurant.insertUser(user)
    }

    suspend fun deleteAllUsers() {
        restaurant.deleteAllUsers()
    }
}