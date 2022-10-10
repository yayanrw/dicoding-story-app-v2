package com.heyproject.storyapp.data.datasource.remote.response

import com.squareup.moshi.Json

data class GeneralResponse(

    @Json(name = "error")
    val error: Boolean? = null,

    @Json(name = "message")
    val message: String? = null
)
