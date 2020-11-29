package com.example.myproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myproject.models.Restaurant

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRestaurantDao(restaurant:Restaurant)

    @Delete
    suspend fun deleteRestaurantDao(restaurant: Restaurant)

    @Query("Delete FROM restaurant_table")
    suspend fun deleteAllDao()

    @Query("SELECT * FROM restaurant_table ORDER BY name")
    fun selectAllRestaurants(): LiveData<List<Restaurant>>
}