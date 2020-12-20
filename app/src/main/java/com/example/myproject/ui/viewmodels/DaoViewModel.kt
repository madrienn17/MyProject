package com.example.myproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.RestaurantRoomDatabase
import com.example.myproject.models.Favorite
import com.example.myproject.models.RestaurantPic
import com.example.myproject.models.User
import com.example.myproject.models.UserPic
import com.example.myproject.repository.RepositoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DaoViewModel(application: Application): AndroidViewModel(application) {
    private val restaurantDao = RestaurantRoomDatabase.getData(application).RestaurantDao()
    private val repository = RepositoryDao(restaurantDao)
    val readAllUsers: LiveData<List<User>> = repository.getAllUsers
    val readAllUserPic: LiveData<List<UserPic>> = repository.getAllUserPics
    val readAllRestPic: LiveData<List<RestaurantPic>> = repository.getAllRestPics

    fun addRestaurantDB(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavorite(favorite)
        }
    }

    fun deleteRestaurantDB(rId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavRest(rId)
        }
    }

    fun deleteAllFavs(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorites()
        }
    }


    fun addUserDB(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun getUserFavorites(userName: String): LiveData<List<Long>>{
            return repository.getUserFavorites(userName)
    }

    fun insertUserPic(userPic: UserPic){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUserPic(userPic)
        }
    }

    fun insertRestPic(restaurantPic: RestaurantPic) {
        viewModelScope.launch (Dispatchers.IO){
            repository.insertRestPic(restaurantPic)
        }
    }
}
