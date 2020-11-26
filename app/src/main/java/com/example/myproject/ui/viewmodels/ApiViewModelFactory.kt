package com.example.myproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.api.RestaurantApiRepository

class ApiViewModelFactory (private val repository: RestaurantApiRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestaurantsListViewModel(repository) as T
    }

}