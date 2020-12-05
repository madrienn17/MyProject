package com.example.myproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite (
        val restId:Long,
        val userId:Long,
        @PrimaryKey(autoGenerate = true)
        val id:Int = 0
 )