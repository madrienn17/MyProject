package com.example.myproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.models.Favorite

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun deleteRestaurantDao(favorite: Favorite)

    @Query("Delete FROM favorites")
    suspend fun deleteAllDao()

    @Query("SELECT * FROM favorites ORDER BY id ASC")
    fun selectAllRestaurants(): LiveData<List<Favorite>>
    @RawQuery
    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int
}