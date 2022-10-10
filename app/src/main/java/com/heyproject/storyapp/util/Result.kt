package com.heyproject.storyapp.util

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 19:24
Github : https://github.com/yayanrw
 **/

sealed class Result<T>(val data: T? = null, val message: String? = null)  {
    class Loading<T> : Result<T>()
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String?, data: T? = null) : Result<T>(data, message)
}