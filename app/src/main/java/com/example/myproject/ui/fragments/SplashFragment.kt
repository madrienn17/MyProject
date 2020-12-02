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
import com.example.myproject.models.Restaurant
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {
    private lateinit var restaurantViewModel: ApiViewModel

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.GONE
       launch {
           val repository = ApiRepository()
           val factory = ApiViewModelFactory(repository)
           restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
           Constants.cities = restaurantViewModel.getCities()
           Constants.countries = restaurantViewModel.getCountries()

           Log.d("CITIES ", Constants.cities.toString())
           Log.d("CITYNR. ", Constants.cities.size.toString())
           Log.d("COUNTRIES ", Constants.countries.toString())

           for (city in 2700 until Constants.cities.size) {
               restaurantViewModel.loadRestaurantsByCity(Constants.cities[city])
           }
           //restaurantViewModel.loadRestaurantsByCountry("CA")
           lateinit var list: List<Restaurant>
//           restaurantViewModel.restaurantsByCountry.observe(requireActivity(), {
//               restaurants -> list = restaurants;setComp(list);Log.d("APIDATA",restaurants.toString())
//           })
           restaurantViewModel.restaurantsByCity.observe(requireActivity(), { restaurants ->
                list = restaurants
                setComp(list)
                Log.d("APIDATA", restaurants.toString())
           })
          // delay(5000)
           withContext(Dispatchers.Main) {
               findNavController().navigate(R.id.navigation_restaurants)
           }
       }
    }

    companion object RestaurantsByCity {
        var list:List<Restaurant> = listOf()
    }

    private fun setComp(li: List<Restaurant>) {
        list = li
    }
}