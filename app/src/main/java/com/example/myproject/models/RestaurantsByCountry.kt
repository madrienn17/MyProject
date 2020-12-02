package com.example.myproject.models

data class RestaurantsByCountry(val total_entries:Int, val per_page:Int, val current_page:Int, val restaurants:List<Restaurant> ) {
}