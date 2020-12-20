package com.example.myproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// model for user profile picture entity
@Entity(tableName = "userPic")
data class UserPic(
        val userName:String,
        @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
        val userPic: String,
        @PrimaryKey(autoGenerate = true)
        val uPicId: Int = 0
        )