package com.example.myproject.ui.fragments

import android.annotation.SuppressLint
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
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var restaurant: Restaurant
    private var model: SharedViewModel?=null
    private val TAG = "DetailsFragment"

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }
    @SuppressLint("CutPasteId")
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

        myView.apply {
            findViewById<TextView>(R.id.details_name).text = name
            findViewById<TextView>(R.id.details_address).text = address
            findViewById<TextView>(R.id.details_city).text = city
            findViewById<TextView>(R.id.details_state).text = state
            findViewById<TextView>(R.id.details_area).text = area
            findViewById<TextView>(R.id.details_postalcode).text = postal_code
            findViewById<TextView>(R.id.details_country).text = country
            findViewById<TextView>(R.id.details_price).text = price
            findViewById<TextView>(R.id.details_reserve_url).text = reserve_url
            findViewById<TextView>(R.id.details_mobile_reserve_url).text = mobile_reserve_url
        }

        val mapButton =myView.findViewById<ImageButton>(R.id.view_location_map)

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

        val reserveUrl = myView.findViewById<TextView>(R.id.details_reserve_url)
        reserveUrl.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reserve_url))
            startActivity(browserIntent)
        }

        val mobileReservation = myView.findViewById<TextView>(R.id.details_mobile_reserve_url)
        mobileReservation.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mobile_reserve_url))
            startActivity(browserIntent)
        }

        return myView
    }

}
