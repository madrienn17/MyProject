package com.example.myproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myproject.models.Favorite
import com.example.myproject.models.User

@Database(entities= [Favorite::class, User::class], version = 5, exportSchema = false)
abstract class RestaurantRoomDatabase : RoomDatabase() {
    abstract fun RestaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantRoomDatabase?=null


        fun getData(context: Context): RestaurantRoomDatabase {
            val temporaryInstance = INSTANCE
            if(temporaryInstance != null) {
                return temporaryInstance
            }
            synchronized(this){
                val instanceLocal = Room.databaseBuilder(
                        context.applicationContext,
                        RestaurantRoomDatabase::class.java,
                        "wheretoeatdao"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instanceLocal
                return instanceLocal
            }
        }
    }
}