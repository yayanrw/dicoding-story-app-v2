package com.heyproject.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.domain.model.Story
import com.heyproject.storyapp.util.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun fetchAllStoryWithLocation(token: String): LiveData<Result<List<Story>>> =
        storyRepository.getAllStoriesWithLocation(token)
}