package com.example.myproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myproject.models.Favorite

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurantDao(favorite: Favorite)

    @Delete
    suspend fun deleteRestaurantDao(favorite: Favorite)

    @Query("Delete FROM favorites")
    suspend fun deleteAllDao()

    @Query("SELECT * FROM favorites ORDER BY id ASC")
    fun selectAllRestaurants(): LiveData<List<Favorite>>
}