package com.example.myproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.data.RestaurantRoomDatabase
import com.example.myproject.models.Favorite
import com.example.myproject.repository.RepositoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DaoViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Favorite>>
    private val repository: RepositoryDao

    init {
        val restaurantDao = RestaurantRoomDatabase.getData(application).RestaurantDao()
        repository = RepositoryDao(restaurantDao)
        readAllData = repository.getAll
    }

    fun addRestaurantDB(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favorite)
        }
    }

    fun deleteRestaurantDB(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRestaurantDao(favorite)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }

    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery) {
        viewModelScope.launch (Dispatchers.IO){
            repository.vacuumDb(supportSQLiteQuery)
        }
    }
}
