package com.example.myproject.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var restaurant: Restaurant
    var position: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt("POSITION")?.let {
            position = it
           Log.d("POSITION:", "$position")
        }
        arguments?.get("Restaurant")?.let {
            restaurant = it as Restaurant
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_details, container, false)
        init()
        return myView
    }

    private fun init() {
       // myView.findViewById<TextView>(R.id.name) = restaurant.name
    }
    /* "id": 107257,
    "name": "Las Tablas Colombian Steak House",
    "address": "2942 N Lincoln Ave",
    "city": "Chicago",
    "state": "IL",
    "area": "Chicago / Illinois",
    "postal_code": "60657",
    "country": "US",
    "phone": "7738712414",
    "lat": 41.935137,
    "lng": -87.662815,
    "price": 2,
    "reserve_url": "http://www.opentable.com/single.aspx?rid=107257",
    "mobile_reserve_url": "http://mobile.opentable.com/opentable/?restId=107257",
    "image_url": */
}