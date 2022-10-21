package com.heyproject.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.util.Result

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<Result<GeneralResponse>> =
        userRepository.register(name, email, password)
}