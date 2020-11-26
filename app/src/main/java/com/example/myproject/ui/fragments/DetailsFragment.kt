package com.example.myproject.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.DetailsViewModel

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var restaurant: Restaurant
    var position: Int = 0
    private var model: DetailsViewModel?=null
    private val TAG = "DetailsFragment"

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

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

//    private fun getStats(service: RestaurantApiService) {
//        val call = service.getStats()
//        call.enqueue(object : Callback<ArrayList<String>> {
//            override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
//                val stats = response.body().toString()
//                Log.d(TAG, "Stats: $stats")
//            }
//            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
//                Log.d(TAG, "Error fetching data!")
//            }
//        })
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_details, container, false)
        init()
        return myView
    }

    private fun init() {
        //myView.findViewById<TextView>(R.id.name).text = restaurant.name
        //myView.findViewById<TextView>(R.id.details_id).text = restaurant.id.toString()
        //TODO(az osszes propertyre)
        //val service = RestaurantApiClient.getInstance().create(RestaurantApiService::class.java)
       // getStats(service)

    }
}
