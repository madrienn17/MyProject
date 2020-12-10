package com.example.myproject.models

data class Restaurant(
    var id:Long,
    var name:String,
    var address:String,
    var city:String,
    var state:String?,
    var area:String,
    var postal_code:String,
    var country:String,
    var phone:String,
    var lat:Double,
    var lng:Double,
    var price:Int,
    var reserve_url:String,
    var mobile_reserve_url: String,
    var image_url:String
    ){

    constructor(): this(-1, "", "", "", "", "", "", "", "", -1.0,-1.0,0
    ,"","","")

    fun setRest(id: Long,name: String, address: String, city: String, state: String?, area: String,
    postal_code: String, country: String, phone: String, lat: Double, price: Int, reserve_url: String,
    mobile_reserve_url: String, image_url: String) {
        this.id = id
        this.name = name
        this.address = address
        this.city = city
        this.state = state
        this.area = area
        this.postal_code = postal_code
        this.country = country
        this.phone = phone
        this.lat = lat
        this.price = price
        this.reserve_url = reserve_url
        this.mobile_reserve_url = mobile_reserve_url
        this.image_url = image_url
    }
}