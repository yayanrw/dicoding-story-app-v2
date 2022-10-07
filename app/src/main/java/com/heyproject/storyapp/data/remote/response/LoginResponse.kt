package com.heyproject.storyapp.data.remote.response

import com.heyproject.storyapp.data.remote.dto.LoginResultDto
import com.squareup.moshi.Json

data class LoginResponse(

    @Json(name = "loginResult")
    val loginResult: LoginResultDto? = null,

    @Json(name = "error")
    val error: Boolean? = null,

    @Json(name = "message")
    val message: String? = null
)
