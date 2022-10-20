package com.heyproject.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.domain.model.Story
import com.heyproject.storyapp.domain.model.toDomain
import com.heyproject.storyapp.util.Result
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<Result<List<Story>>>()
    val stories: LiveData<Result<List<Story>>> = _stories

    fun fetchAllStoryWithLocation(token: String) = viewModelScope.launch {
        _stories.value = Result.Loading()
        try {
            val response = storyRepository.getAllStoriesWithLocation(token)
            if (!response.error) {
                _stories.value = Result.Success(response.listStory.map { storyDto ->
                    storyDto.toDomain()
                })
            } else {
                _stories.value = Result.Error(response.message)
            }
        } catch (e: Exception) {
            _stories.value = Result.Error(e.message)
        }
    }
}