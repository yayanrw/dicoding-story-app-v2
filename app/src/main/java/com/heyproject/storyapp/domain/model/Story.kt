package com.heyproject.storyapp.domain.model

import com.squareup.moshi.Json

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 14:59
Github : https://github.com/yayanrw
 **/

data class Story(
    val photoUrl: String?,
    val createdAt: String?,
    val name: String?,
    val description: String?,
    val lon: Double?,
    val id: String?,
    val lat: Double?
)
