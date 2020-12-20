package com.example.myproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
// restaurant picture entity
@Entity(tableName = "restaurantPic")
data class RestaurantPic(
        val restName:String,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        val restPic: ByteArray,
        @PrimaryKey(autoGenerate = true)
        val restPicId:Int = 0
        ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantPic

        if (restName != other.restName) return false
        if (!restPic.contentEquals(other.restPic)) return false
        if (restPicId != other.restPicId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restName.hashCode()
        result = 31 * result + restPic.contentHashCode()
        result = 31 * result + restPicId
        return result
    }
}