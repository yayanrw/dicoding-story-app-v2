package com.heyproject.storyapp.ui.story_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.heyproject.storyapp.model.User
import com.heyproject.storyapp.model.UserPreference

class StoryDetailViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}