package com.heyproject.storyapp.ui.story_add

import androidx.lifecycle.*
import com.heyproject.storyapp.model.User
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.network.StoryApi
import com.heyproject.storyapp.util.RequestState
import com.heyproject.storyapp.util.reduceFileImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class StoryAddViewModel(private val pref: UserPreference) : ViewModel() {
    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    fun uploadImage(getFile: File, description: String) {
        val token = runBlocking {
            pref.getUser().first().token
        }
        viewModelScope.launch {
            try {
                _requestState.value = RequestState.LOADING
                val file = reduceFileImage(getFile)
                val descRequestBody = description.toRequestBody("text/plain".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                val response = StoryApi.retrofitService.insertStory(
                    photo = imageMultipart,
                    description = descRequestBody,
                    auth = "Bearer $token"
                )

                if (!response.error!!) {
                    _requestState.value = RequestState.SUCCESS
                } else {
                    _requestState.value = RequestState.ERROR
                }
            } catch (e: HttpException) {
                _requestState.value = RequestState.ERROR
            } catch (e: IOException) {
                _requestState.value = RequestState.NO_CONNECTION
            }
        }
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}