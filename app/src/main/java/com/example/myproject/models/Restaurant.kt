package com.example.myproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_table")
data class Restaurant(
    @PrimaryKey(autoGenerate = false)
    var id:Long,
    var name:String,
    var address:String,
    val city:String,
    val state:String,
    val area:String,
    val postal_code:String,
    val country:String,
    val phone:String,
    val lat:Double,
    val lng:Double,
    var price:Int,
    val reserve_url:String,
    val mobile_reserve_url: String,
    val image_url:String
    ){
}