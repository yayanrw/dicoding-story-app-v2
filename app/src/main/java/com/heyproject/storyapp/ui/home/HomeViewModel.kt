package com.heyproject.storyapp.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.domain.model.Story
import com.heyproject.storyapp.domain.model.User
import com.heyproject.storyapp.domain.model.toDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun fetchStories(token: String): LiveData<PagingData<Story>> =
        storyRepository.getStories(token).map { result ->
            result.map { storyEntity ->
                storyEntity.toDomain()
            }
        }.cachedIn(viewModelScope)

    fun logOut() = viewModelScope.launch {
        userRepository.logOut()
    }

    fun fetchUser() = viewModelScope.launch {
        _user.value = userRepository.getUser().first()
    }

}