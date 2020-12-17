package com.example.myproject.models

class RestaurantResponse(val total_entries:Int, val page:Int, val per_page:Int, val restaurants:List<Restaurant>) {
}