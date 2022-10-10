package com.heyproject.storyapp.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.heyproject.storyapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 17:05
Github : https://github.com/yayanrw
 **/

class UserDataStore(private val dataStore: DataStore<Preferences>) {
    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USERID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = ""
            preferences[NAME_KEY] = ""
            preferences[TOKEN_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDataStore? = null
        private val USERID_KEY = stringPreferencesKey("userID")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}