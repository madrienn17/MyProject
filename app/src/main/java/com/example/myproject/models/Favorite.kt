package com.example.myproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite (
        val restId:Long,
        val userName:String,
        @PrimaryKey(autoGenerate = true)
        val id:Int = 0
 )