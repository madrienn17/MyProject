package com.example.myproject.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myproject.models.Restaurant

class RestaurantViewModel: ViewModel() {
    private val restaurantList: ArrayList<Restaurant> = arrayListOf()

    fun addRestaurant(restaurant: Restaurant) {
        restaurantList.add(restaurant)
    }

    fun getRestaurant(id: Int) = restaurantList.getOrNull(id)

    fun getAllRestaurants() = restaurantList

    fun removeRestaurant(pos:Int) = restaurantList.removeAt(pos)

    fun generateDummyData(nr: Int) {
        for(i in 0..nr) {
            Log.d("lefut", i.toString())
            val restaurant = Restaurant((i*258-96).toLong(),"Restaurant ${i}","street ${i}", i.toString(), "US", "abc", (14586+i).toLong(), "America", (4586786+i/8).toLong(), 7678-i*95*10.00, (i*5-65).toDouble(),
            i, "aerfcaf.com", "aerfaeagryh.ro", "http://www.ondiseno.com/fotos_proyectos/360/grans/235301.jpg")
            restaurantList.add(restaurant)
        }
    }
}