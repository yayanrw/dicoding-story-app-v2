package com.heyproject.storyapp.data.remote.dto

import com.squareup.moshi.Json

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 14:41
Github : https://github.com/yayanrw
 **/

data class LoginResultDto(

    @Json(name = "name")
    val name: String?,

    @Json(name = "userId")
    val userId: String?,

    @Json(name = "token")
    val token: String?
)
