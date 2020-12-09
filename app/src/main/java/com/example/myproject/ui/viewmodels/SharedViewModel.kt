package com.example.myproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myproject.models.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SharedViewModel() : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    var isLoggedIn = false

    val favorited: HashMap<String, ArrayList<Restaurant>> = hashMapOf()

    fun addFav(uName:String, rest:Restaurant) {
        var temp = favorited.get(uName)
        if(temp == null){
            temp = arrayListOf(rest)
            favorited[uName] = temp
        }
        else {
            temp.add(rest)
        }
    }

    fun getUserFavorites(uName:String): List<Restaurant> {
        if(favorited.containsKey(uName)) {
            return favorited.getValue(uName)
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