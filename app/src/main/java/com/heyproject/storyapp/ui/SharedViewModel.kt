package com.heyproject.storyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
Written by Yayan Rahmat Wijaya on 10/19/2022 22:07
Github : https://github.com/yayanrw
 **/

class SharedViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
            _user.value = user
        }
    }

    fun fetchUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser().first()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _user.value = User("", "", "", false)
            userRepository.logOut()
        }
    }

}