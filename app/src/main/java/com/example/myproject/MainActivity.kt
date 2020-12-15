package com.example.myproject

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_restaurants, R.id.navigation_splash, R.id.navigation_details, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        appContext = applicationContext
        daoViewModel = ViewModelProvider(this).get(DaoViewModel::class.java)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    companion object: CoroutineScope {
        var isLoggedIn = false
        lateinit var daoViewModel: DaoViewModel
        lateinit var restaurantViewModel:ApiViewModel
        lateinit var appContext: Context

        fun getUserFavorites(uName:String): List<Restaurant> {
            val favs = daoViewModel.getUserFavorites(uName).value
            var favrests:MutableList<Restaurant> = mutableListOf()
            launch {
                val repository = ApiRepository()
                val factory = ApiViewModelFactory(repository)
                restaurantViewModel = ViewModelProvider(ViewModelStore(), factory).get(ApiViewModel::class.java)
                if (favs != null) {
                    for (i in favs) {
                        val restresp = restaurantViewModel.getRestaurantsById(i.toInt())
                        if (restresp.isSuccessful && restresp.body() != null) {
                            val respbody = restresp.body()
                            if (respbody != null) {
                                favrests.add(respbody)
                            }
                        }
                        else {
                            Toast.makeText(appContext,"Cannot load favorites because of API!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    favrests = Constants.emptyRest.toMutableList()
                }
            }
            return favrests
        }

        override val coroutineContext: CoroutineContext
            get() =  Dispatchers.Main + Job()
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication().getSystemService(
//                Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//        return when {
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//    }
}