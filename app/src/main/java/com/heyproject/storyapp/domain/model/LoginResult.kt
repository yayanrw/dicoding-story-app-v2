package com.heyproject.storyapp.domain.model

import com.heyproject.storyapp.data.datasource.remote.dto.LoginResultDto

/**
Written by Yayan Rahmat Wijaya on 10/12/2022 15:37
Github : https://github.com/yayanrw
 **/

data class LoginResult(
    val name: String, val userId: String, val token: String
)

fun LoginResultDto.toDomain(): LoginResult {
    return LoginResult(
        name = name.orEmpty(),
        userId = userId.orEmpty(),
        token = token.orEmpty(),
    )
}

fun LoginResultDto.toLoggedInUser(): User {
    return User(
        userId = userId.orEmpty(),
        name = name.orEmpty(),
        token = token.orEmpty(),
        isLogin = true
    )
}

fun LoginResult.toLoggedInUser(): User {
    return User(
        userId = userId,
        name = name,
        token = token,
        isLogin = true
    )
}