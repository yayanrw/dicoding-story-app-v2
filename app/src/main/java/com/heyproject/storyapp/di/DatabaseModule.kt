package com.heyproject.storyapp.di

import android.content.Context
import androidx.room.Room
import com.heyproject.storyapp.data.datasource.local.dao.RemoteKeysDao
import com.heyproject.storyapp.data.datasource.local.dao.StoryDao
import com.heyproject.storyapp.data.datasource.local.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 10:40
Github : https://github.com/yayanrw
 **/

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }
}