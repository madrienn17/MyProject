package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.api.RestaurantApiRepository
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.RestaurantsListViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {
    private lateinit var restaurantViewModel: RestaurantsListViewModel

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       launch {
           val repository = RestaurantApiRepository()
           val factory = ApiViewModelFactory(repository)
           restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(RestaurantsListViewModel::class.java)
           lateinit var list : List<Restaurant>

           restaurantViewModel.loadRestaurants("London")

           restaurantViewModel.restaurants.observe(requireActivity(), { restaurants ->
               list = restaurants
               Log.d("APIDATA", restaurants[10].toString())
           })

           delay(5000)
           withContext(Dispatchers.Main) {
               findNavController().navigate(R.id.navigation_restaurants)
           }
       }

    }
}