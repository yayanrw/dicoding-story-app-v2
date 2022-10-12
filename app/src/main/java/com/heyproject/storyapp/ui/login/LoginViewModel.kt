package com.heyproject.storyapp.ui.login

import androidx.lifecycle.*
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.domain.model.LoginResult
import com.heyproject.storyapp.domain.model.User
import com.heyproject.storyapp.domain.model.toDomain
import com.heyproject.storyapp.domain.model.toLoggedInUser
import com.heyproject.storyapp.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Result<LoginResult>>()
    val loginState: LiveData<Result<LoginResult>> = _loginState

    fun signIn(
        email: String, password: String
    ) {
        viewModelScope.launch {
            _loginState.value = Result.Loading()
            try {
                val response = userRepository.logIn(email, password)

                if (response.error == false) {
                    _loginState.value = Result.Success(response.loginResult!!.toDomain())
                    userRepository.saveUser(response.loginResult.toLoggedInUser())
                } else {
                    _loginState.value = Result.Error(response.message.toString())
                }
            } catch (e: Exception) {
                _loginState.value = Result.Error(e.message.toString())
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            userRepository.logOut()
        }
    }
}