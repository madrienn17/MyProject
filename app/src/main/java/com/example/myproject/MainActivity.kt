package com.example.myproject

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.firstapplication.R
import com.example.myproject.models.Favorite
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val sharedViewModel: SharedViewModel by viewModels ()
    val daoViewModel: DaoViewModel by viewModels()

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
//        val restaurantDataModel: RestaurantDataModel by viewModels()
        //restaurantViewModel.generateDummyData(10)

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    override fun onDestroy() {
        val favorites = restaurantToFav(sharedViewModel.favorited)
        for (i in favorites) {
            daoViewModel.addRestaurantDB(i)
        }
        super.onDestroy()
    }

    private fun restaurantToFav(favs: HashMap<String, ArrayList<Restaurant>>) : ArrayList<Favorite> {
        val fav :ArrayList<Favorite> = arrayListOf()
        for (f in favs) {
            for(r in f.value) {
                fav.add(Favorite(r.id,f.key))
            }
        }
        return fav
    }
}