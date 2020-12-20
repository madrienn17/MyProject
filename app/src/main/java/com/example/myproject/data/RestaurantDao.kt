package com.example.myproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myproject.models.Favorite
import com.example.myproject.models.RestaurantPic
import com.example.myproject.models.User
import com.example.myproject.models.UserPic

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE restId=:rId")
    suspend fun deleteRestaurantDao(rId:Long)

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPic(userPic: UserPic)

    @Query("SELECT * from userPic ")
    fun selectUserPics(): LiveData<List<UserPic>>

    @Query("DELETE from userPic")
    suspend fun deleteUserPics()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRestPic(restaurantPic: RestaurantPic)

    @Query("SELECT * from restaurantPic")
    fun selectRestPics() : LiveData<List<RestaurantPic>>

    @Query("DELETE from restaurantPic")
    suspend fun deleteRestPic()
}