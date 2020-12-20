package com.example.myproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_maps)
        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF40C4FF")))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     **/

    override fun onMapReady(gMap: GoogleMap) {
        this.gMap = gMap

        val latitude = intent.getDoubleExtra("lat",-33.8523341)
        val longitude = intent.getDoubleExtra("lng", 151.2106085)
        val restName = intent.getStringExtra("name")

        val zoom = 15.toFloat()

        val latLng = LatLng(latitude,longitude)
        gMap.addMarker(MarkerOptions().position(latLng).title(restName))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }
}