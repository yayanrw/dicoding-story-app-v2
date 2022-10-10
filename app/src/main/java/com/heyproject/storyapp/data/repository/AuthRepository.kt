package com.heyproject.storyapp.data.repository

import com.heyproject.storyapp.data.local.UserPreferencesDataStore
import com.heyproject.storyapp.data.remote.api.StoryService
import com.heyproject.storyapp.data.remote.response.GeneralResponse
import com.heyproject.storyapp.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 16:59
Github : https://github.com/yayanrw
 **/

class AuthRepository @Inject constructor(
    private val storyService: StoryService,
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    suspend fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = storyService.postLogin(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(
        name: String, email: String, password: String
    ): Flow<Result<GeneralResponse>> = flow {
        try {
            val response = storyService.postRegister(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        userPreferencesDataStore.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> = userPreferencesDataStore.getAuthToken()
}