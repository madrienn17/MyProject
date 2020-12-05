package com.example.myproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myproject.models.Favorite

@Database(entities= [Favorite::class], version = 3, exportSchema = false)
abstract class RestaurantRoomDatabase : RoomDatabase() {
    abstract fun RestaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantRoomDatabase?=null


        private val migration_1_2: Migration = object: Migration(1,2)
        {
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("CREATE TABLE IF NOT EXISTS favorites (`userId` INTEGER NOT NULL, `restId` INTEGER NOT NULL,`id` INTEGER NOT NULL , PRIMARY KEY(id))")
            }
        }

        fun getData(context: Context): RestaurantRoomDatabase {
            val temporaryInstance = INSTANCE
            if(temporaryInstance != null) {
                return temporaryInstance
            }
            synchronized(this){
                val instanceLocal = Room.databaseBuilder(
                        context.applicationContext,
                        RestaurantRoomDatabase::class.java,
                        "favorites"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instanceLocal
                return instanceLocal
            }
        }
    }
}