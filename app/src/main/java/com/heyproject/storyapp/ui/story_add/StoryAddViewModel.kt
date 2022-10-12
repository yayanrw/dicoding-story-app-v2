package com.heyproject.storyapp.ui.story_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.util.Result
import com.heyproject.storyapp.util.reduceFileImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryAddViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    
    private val _uploadState = MutableLiveData<Result<GeneralResponse>>()
    val uploadState: LiveData<Result<GeneralResponse>> = _uploadState

    fun uploadImage(getFile: File, description: String) {
        viewModelScope.launch {
            try {
                val token = userRepository.getUser().first().token
                _uploadState.value = Result.Loading()
                val file = reduceFileImage(getFile)
                val descRequestBody = description.toRequestBody("text/plain".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                val response = storyRepository.uploadStory(token, imageMultipart, descRequestBody)

                if (response.error == false) {
                    _uploadState.value = Result.Success(response)
                } else {
                    _uploadState.value = Result.Error(response.message.toString())
                }
            } catch (e: Exception) {
                _uploadState.value = Result.Error(e.message.toString())
            }
        }
    }
}