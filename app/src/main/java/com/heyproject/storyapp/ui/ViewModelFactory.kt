package com.heyproject.storyapp.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.di.Injection
import com.heyproject.storyapp.ui.home.HomeViewModel
import com.heyproject.storyapp.ui.login.LoginViewModel
import com.heyproject.storyapp.ui.register.RegisterViewModel
import com.heyproject.storyapp.ui.story_add.StoryAddViewModel
import com.heyproject.storyapp.ui.story_detail.StoryDetailViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_datastore")

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(StoryAddViewModel::class.java) -> {
                StoryAddViewModel(pref) as T
            }
            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) -> {
                StoryDetailViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                ViewModelFactory(
                    Injection.provideUserRepository(context.dataStore),
                    Injection.provideStoryRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}