package com.heyproject.storyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 13:51
Github : https://github.com/yayanrw
 **/

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
