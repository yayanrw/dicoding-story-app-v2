package com.heyproject.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.domain.model.LoginResult
import com.heyproject.storyapp.util.Result

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signIn(email: String, password: String): LiveData<Result<LoginResult>> =
        userRepository.logIn(email, password)
}