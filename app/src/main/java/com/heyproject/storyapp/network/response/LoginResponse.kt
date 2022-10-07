package com.heyproject.storyapp.network.response

import com.heyproject.storyapp.model.User
import com.squareup.moshi.Json

data class LoginResponse(

    @Json(name = "loginResult")
    val loginResult: LoginResult? = null,

    @Json(name = "error")
    val error: Boolean? = null,

    @Json(name = "message")
    val message: String? = null
)

data class LoginResult(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "userId")
    val userId: String? = null,

    @Json(name = "token")
    val token: String? = null
) {
    fun toLoginUser(loginResult: LoginResult): User {
        return User(
            userId = loginResult.userId!!,
            name = loginResult.name!!,
            token = loginResult.token!!,
            isLogin = true
        )
    }
}
