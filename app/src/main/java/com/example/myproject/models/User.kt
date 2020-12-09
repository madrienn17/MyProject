package com.example.myproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
        val name:String,
        val address:String,
        val phone:String,
        val email:String,
        val password:String,
        @PrimaryKey(autoGenerate = true)
        val id:Int=0
        )