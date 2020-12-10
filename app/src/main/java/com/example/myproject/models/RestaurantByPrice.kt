package com.example.myproject.models

data class RestaurantByPrice(
        val total_entries: Int,
        val page: Int,
        val per_page: Int,
        val restaurants: List<Restaurant>
)
