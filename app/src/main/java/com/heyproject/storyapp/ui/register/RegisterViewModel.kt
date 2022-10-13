package com.heyproject.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.util.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerState = MutableLiveData<Result<GeneralResponse>>()
    val registerState: LiveData<Result<GeneralResponse>> = _registerState

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        try {
            _registerState.value = Result.Loading()
            val response = userRepository.register(name, email, password)

            if (!response.error!!) {
                _registerState.value = Result.Success(response)
            } else {
                _registerState.value = Result.Error(response.message.toString())
            }
        } catch (e: Exception) {
            _registerState.value = Result.Error(e.message.toString())
        }
    }
}