package com.heyproject.storyapp.model

data class User(
    var userId: String,
    var name: String,
    var token: String,
    var isLogin: Boolean
)
