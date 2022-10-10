package com.heyproject.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.heyproject.storyapp.data.datasource.local.database.StoryDatabase
import com.heyproject.storyapp.data.datasource.local.datastore.UserDataStore
import com.heyproject.storyapp.data.datasource.remote.api.StoryApi
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.data.repository.UserRepository

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 19:18
Github : https://github.com/yayanrw
 **/

object Injection {

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val api = StoryApi.retrofitService
        val pref = UserDataStore.getInstance(dataStore)
        return UserRepository.getInstance(api, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val api = StoryApi.retrofitService
        return StoryRepository(database, api)
    }

}