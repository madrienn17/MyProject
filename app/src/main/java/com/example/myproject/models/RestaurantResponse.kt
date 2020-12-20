package com.example.myproject.models

// model for getting list of restaurants
class RestaurantResponse(val total_entries:Int, val page:Int, val per_page:Int, val restaurants:List<Restaurant>) {
}