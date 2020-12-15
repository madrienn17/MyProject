package com.example.myproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.models.Favorite
import com.example.myproject.models.User

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun deleteRestaurantDao(favorite: Favorite)

    @Query("Delete FROM favorites")
    suspend fun deleteAllFavorites()

    @Query("SELECT * FROM favorites ORDER BY id ASC")
    fun selectAllRestaurants(): LiveData<List<Favorite>>
    @RawQuery
    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery): Int

    @Query("SELECT restId FROM favorites WHERE UserName = :userName")
    fun getUserFavorites(userName:String): LiveData<List<Long>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM  user ORDER BY id ASC")
    fun selectAllUsers(): LiveData<List<User>>

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()

}