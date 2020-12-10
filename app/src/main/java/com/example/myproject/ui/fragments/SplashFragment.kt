package com.example.myproject.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

        val loginButton: Button = view.findViewById(R.id.login_register)
        loginButton.setOnClickListener{
            findNavController().navigate(R.id.navigation_login)
        }

        launch {
            val repository = ApiRepository()
            val factory = ApiViewModelFactory(repository)
            restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)

            val cityResponse = restaurantViewModel.getCities()
            val countryResponse = restaurantViewModel.getCountries()

            if (cityResponse.isSuccessful && countryResponse.isSuccessful) {
                Constants.cities = cityResponse.body()?.cities
                Constants.countries = countryResponse.body()?.countries
            }
            else {
                Toast.makeText(context,"Cannot load the data from API!",Toast.LENGTH_SHORT).show()
                delay(5000)
            }

            Log.d("CITIES ", Constants.cities.toString())
            Log.d("COUNTRIES ", Constants.countries.toString())

            withContext(Dispatchers.Main) {
                favoritesList = daoViewModel.readAllData
                favoritesList!!.observe(requireActivity(),{
                    if( favoritesList != null) {
                        for (fav in favoritesList!!.value?.toList()!!) {
                            val converted = Restaurant()
                            convertFavToRestaurant(fav, converted)
                           sharedViewModel.addFav(Constants.USER_NAME, converted)
                        }
                    }
                })

               // delay(3000)
            }
        }

    }

    private fun convertFavToRestaurant(favorite: Favorite,  converted:Restaurant) {
        val id = favorite.restId
        var restaurant: Restaurant
        var r : Restaurant

        launch {
            val repository = ApiRepository()
            val factory = ApiViewModelFactory(repository)
            restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
            val restresp = restaurantViewModel.getRestaurantsById(id.toInt())
            if (restresp.isSuccessful) {
                val respbody = restresp.body()
                if (respbody != null) {
                    restaurant = respbody
                    r = restaurant
                    converted.setRest(r.id, r.name, r.address, r.city, r.state, r.area, r.postal_code, r.country,
                            r.phone, r.lat, r.price, r.reserve_url, r.mobile_reserve_url, r.image_url)
                }
            }
            else {
                Toast.makeText(context,"Cannot load favorites because of API!",Toast.LENGTH_SHORT).show()
            }
        }
    }

}