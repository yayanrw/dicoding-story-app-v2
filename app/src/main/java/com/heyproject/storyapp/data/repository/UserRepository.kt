package com.heyproject.storyapp.data.repository

import com.heyproject.storyapp.data.datasource.local.datastore.UserDataStore
import com.heyproject.storyapp.data.datasource.remote.api.StoryService
import com.heyproject.storyapp.domain.model.User

/**
Written by Yayan Rahmat Wijaya on 10/7/2022 16:59
Github : https://github.com/yayanrw
 **/

class UserRepository(
    private val storyService: StoryService,
    private val userDataStore: UserDataStore
) {
    suspend fun logIn(email: String, password: String) = storyService.postLogin(email, password)

    suspend fun register(name: String, email: String, password: String) =
        storyService.postRegister(name, email, password)

    suspend fun saveUser(user: User) = userDataStore.saveUser(user)

    fun getUser() = userDataStore.getUser()

    suspend fun logOut() = userDataStore.logOut()


    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(storyService: StoryService, userDataStore: UserDataStore): UserRepository {
            return INSTANCE ?: synchronized(this) {
                UserRepository(storyService, userDataStore).also {
                    INSTANCE = it
                }
            }
        }
    }
}