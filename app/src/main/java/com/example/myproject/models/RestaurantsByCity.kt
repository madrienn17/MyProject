package com.example.myproject.models

data class RestaurantsByCity(val count:Int, val per_page:Int, val current_page:Int, val restaurants:List<Restaurant> ){

}
