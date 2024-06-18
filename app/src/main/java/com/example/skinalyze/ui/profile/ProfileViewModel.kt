package com.example.skinalyze.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.ProfileResponse
import kotlinx.coroutines.launch
import com.example.skinalyze.data.repository.Result

class ProfileViewModel(private val userRepository: UserRepository
) : ViewModel() {

    val profileResult: LiveData<Result<ProfileResponse>> get() = userRepository.profileResult

    fun getProfile(): LiveData<Result<ProfileResponse>>{
        return userRepository.getProfile()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}

