package com.example.myproject.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.data.RestaurantDao
import com.example.myproject.models.Favorite
import com.example.myproject.models.RestaurantPic
import com.example.myproject.models.User
import com.example.myproject.models.UserPic

class RepositoryDao(private val restaurant: RestaurantDao) {

    // selecting with livedata
    val getAllUsers: LiveData<List<User>> = restaurant.selectAllUsers()
    val getAllUserPics: LiveData<List<UserPic>> = restaurant.selectUserPics()
    val getAllRestPics: LiveData<List<RestaurantPic>> = restaurant.selectRestPics()

    // favorites
    suspend fun insertFavorite(favorite: Favorite) {
        restaurant.insertFavorite(favorite)
    }
    suspend fun deleteFavRest(rId:Long) {
        restaurant.deleteFavorite(rId)
    }
    suspend fun deleteFavorites() {
        restaurant.deleteAllFavorites()
    }
    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int {
        return restaurant.vacuumDb(supportSQLiteQuery)
    }

    //users
    suspend fun insertUser(user:User) {
        restaurant.insertUser(user)
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

    // restaurantPic
    suspend fun insertRestPic(restaurantPic: RestaurantPic) {
        restaurant.insertRestPic(restaurantPic)
    }
}