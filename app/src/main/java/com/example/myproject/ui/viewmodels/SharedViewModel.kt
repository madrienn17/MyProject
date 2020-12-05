package com.example.myproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myproject.models.Restaurant

class SharedViewModel : ViewModel() {

    val favorited: HashMap<Long, ArrayList<Restaurant>> = hashMapOf()

    fun addFav(uId:Long, rest:Restaurant) {
        var temp = favorited.get(uId)
        if(temp == null){
            temp = arrayListOf(rest)
            favorited[uId] = temp
        }
        else {
            temp.add(rest)
        }
    }

    fun getUserFavorites(uId:Long): List<Restaurant> {
        if(favorited.containsKey(uId)) {
            return favorited.getValue(uId)
        }
        return emptyList()
    }

    fun removeFavorite(restaurant: Restaurant) {
        for(fav in favorited) {
            if(fav.value.contains(restaurant)) {
                fav.value.remove(restaurant)
            }
        }
    }
}