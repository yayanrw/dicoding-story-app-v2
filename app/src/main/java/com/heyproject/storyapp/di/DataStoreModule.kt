package com.heyproject.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.heyproject.storyapp.data.datasource.local.UserPreferencesDataStore
import com.heyproject.storyapp.model.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 10:42
Github : https://github.com/yayanrw
 **/

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideAuthPreferences(dataStore: DataStore<Preferences>): UserPreferencesDataStore =
        UserPreferencesDataStore(dataStore)
}