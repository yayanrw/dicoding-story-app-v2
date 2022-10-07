package com.heyproject.storyapp.ui.home

import androidx.lifecycle.*
import com.heyproject.storyapp.model.User
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.network.StoryApi
import com.heyproject.storyapp.network.response.ListStoryItem
import com.heyproject.storyapp.util.RequestState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(private val pref: UserPreference) : ViewModel() {
    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    private val _stories = MutableLiveData<List<ListStoryItem>?>()
    val stories: LiveData<List<ListStoryItem>?> = _stories

    fun fetchStories() {
        val token = runBlocking {
            pref.getUser().first().token
        }
        viewModelScope.launch {
            try {
                _requestState.value = RequestState.LOADING
                val responseListStories = StoryApi.retrofitService.getStories(
                    1,
                    10,
                    0,
                    "Bearer $token"
                ).listStory
                _stories.value = responseListStories
                if (responseListStories.isNullOrEmpty()) {
                    _requestState.value = RequestState.NO_DATA
                } else {
                    _requestState.value = RequestState.SUCCESS
                }
            } catch (e: HttpException) {
                _requestState.value = RequestState.ERROR
                _stories.value = listOf()
            } catch (e: IOException) {
                _requestState.value = RequestState.NO_CONNECTION
                _stories.value = listOf()
            }
        }
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}