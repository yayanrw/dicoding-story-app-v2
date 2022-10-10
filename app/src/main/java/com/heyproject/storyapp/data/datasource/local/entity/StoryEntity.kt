package com.heyproject.storyapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 13:41
Github : https://github.com/yayanrw
 **/

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,

    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    val lon: Double?,

    val lat: Double?
)
