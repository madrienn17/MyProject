package com.example.myproject.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var restaurant: Restaurant
    private var model: DetailsViewModel?=null
    private val TAG = "DetailsFragment"

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
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
                              savedInstanceState: Bundle?): View {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.VISIBLE
        myView = inflater.inflate(R.layout.fragment_details, container, false)
        val name = requireArguments().get("name").toString()
        val address = requireArguments().get("address").toString()
        val city = requireArguments().get("city").toString()
        val state = requireArguments().get("state").toString()
        val area = requireArguments().get("area").toString()
        val postal_code = requireArguments().get("postal_code").toString()
        val country  = requireArguments().get("country").toString()
        val price = requireArguments().get("price").toString()
        val lat = requireArguments().get("lat").toString()
        val lng = requireArguments().get("lng").toString()
        val phone = requireArguments().get("phone").toString()
        val reserve_url = requireArguments().get("reserve_url").toString()
        val mobile_reserve_url = requireArguments().get("mobile_reserve_url").toString()

        myView.findViewById<TextView>(R.id.details_name).text = name
        myView.findViewById<TextView>(R.id.details_address).text = address
        myView.findViewById<TextView>(R.id.details_city).text = city
        myView.findViewById<TextView>(R.id.details_state).text = state
        myView.findViewById<TextView>(R.id.details_area).text = area
        myView.findViewById<TextView>(R.id.details_postalcode).text = postal_code
        myView.findViewById<TextView>(R.id.details_country).text = country
        myView.findViewById<TextView>(R.id.details_price).text = price
        myView.findViewById<TextView>(R.id.details_reserve_url).text = reserve_url
        myView.findViewById<TextView>(R.id.details_mobile_reserve_url).text = mobile_reserve_url

        val mapButton = myView.findViewById<ImageButton>(R.id.view_location_map)

        mapButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        val callButton = myView.findViewById<ImageButton>(R.id.call_the_place)
        callButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }

        return myView
    }

}
