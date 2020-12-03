package com.example.myproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite
(
        @PrimaryKey(autoGenerate = false)
        val id:Long,
        val userId:Long
 ) {
}