package com.example.myproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.ui.adapters.RestaurantAdapter
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {
    private lateinit var restaurantViewModel: ApiViewModel
    private lateinit var adapter: RestaurantAdapter

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.GONE
       launch {
           delay(5000)
           withContext(Dispatchers.Main) {
               findNavController().navigate(R.id.navigation_restaurants)
           }
       }
    }
}