package com.example.myproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.ui.adapters.RestaurantAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class RestaurantsListFragment: Fragment(){
    private lateinit var restaurantList: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)


        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)

        restaurantAdapter = RestaurantAdapter(requireContext())
        restaurantAdapter.setData(SplashFragment.list)
        restaurantList = root.findViewById(R.id.recyclerView)
        restaurantList.adapter = restaurantAdapter
        restaurantList.layoutManager = LinearLayoutManager(activity)
        restaurantList.setHasFixedSize(true)

        restaurantList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && navBar!!.isShown) {
                    navBar?.visibility = View.GONE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    navBar?.visibility = View.VISIBLE
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return root
    }

}
