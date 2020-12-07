package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.models.Favorite
import com.example.myproject.models.Restaurant
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {
    private lateinit var restaurantViewModel: ApiViewModel
    private  var favoritesList: LiveData<List<Favorite>>? = null
    private val daoViewModel: DaoViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

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

            withContext(Dispatchers.Main) {

                    favoritesList = daoViewModel.readAllData
                favoritesList!!.observe(requireActivity(),{
                    if( favoritesList != null) {
                        for (fav in favoritesList!!.value?.toList()!!) {
                            val converted = Restaurant()
                            convertFavToRestaurant(fav, converted)
                           sharedViewModel.addFav(Constants.USER_ID, converted)
                        }
                    }
                })

                delay(3000)
                findNavController().navigate(R.id.navigation_restaurants)
            }
        }

    }

    private fun convertFavToRestaurant(favorite: Favorite,  converted:Restaurant) {
        val id = favorite.restId
        var restaurant: Restaurant

        launch {
            val repository = ApiRepository()
            val factory = ApiViewModelFactory(repository)
            restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
            restaurant = restaurantViewModel.getRestaurantsById(id.toInt())
            val r = restaurant
            converted.setRest(r.id, r.name, r.address, r.city, r.state, r.area, r.postal_code, r.country,
                    r.phone, r.lat, r.price, r.reserve_url, r.mobile_reserve_url, r.image_url)
        }
    }

//    companion object RestaurantsByCity {
//        var list:List<Restaurant> = listOf()
//    }
//
//    private fun setComp(li: List<Restaurant>) {
//        list = li
//    }
}