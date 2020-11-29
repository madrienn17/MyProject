package com.example.myproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myproject.models.Restaurant

@Database(entities= [Restaurant::class], version = 1, exportSchema = false)
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
                        "restaurant_database"
                ).build()
                INSTANCE = instanceLocal
                return instanceLocal
            }
        }
    }
}