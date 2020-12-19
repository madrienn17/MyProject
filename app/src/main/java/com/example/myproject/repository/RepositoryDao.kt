package com.example.myproject.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.data.RestaurantDao
import com.example.myproject.models.Favorite
import com.example.myproject.models.RestaurantPic
import com.example.myproject.models.User
import com.example.myproject.models.UserPic

class RepositoryDao(private val restaurant: RestaurantDao) {
    val getAll: LiveData<List<Favorite>> = restaurant.selectAllRestaurants()
    val getAllUsers: LiveData<List<User>> = restaurant.selectAllUsers()
    val getAllUserPics: LiveData<List<UserPic>> = restaurant.selectUserPics()
    val getAllRestPics: LiveData<List<RestaurantPic>> = restaurant.selectRestPics()

    suspend fun insert(favorite: Favorite) {
        restaurant.insert(favorite)
    }

    suspend fun deleteRestaurantDao(favorite: Favorite) {
        restaurant.deleteRestaurantDao(favorite)
    }

    suspend fun deleteFavorites() {
        restaurant.deleteAllFavorites()
    }

    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int {
        return restaurant.vacuumDb(supportSQLiteQuery)
    }

    suspend fun insertUser(user:User) {
        restaurant.insertUser(user)
    }

    suspend fun deleteAllUsers() {
        restaurant.deleteAllUsers()
    }

    fun getUserFavorites(userName:String): LiveData<List<Long>> {
       return restaurant.getUserFavorites(userName)
    }

    suspend fun insertUserPic(userPic: UserPic) {
        restaurant.insertUserPic(userPic)
    }

    suspend fun deleteUserPics(){
        restaurant.deleteUserPics()
    }

    suspend fun insertRestPic(restaurantPic: RestaurantPic) {
        restaurant.insertRestPic(restaurantPic)
    }
}