package com.example.skinalyze.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.ProfileResponse
import kotlinx.coroutines.launch
import com.example.skinalyze.data.repository.Result

class ProfileViewModel(private val userRepository: UserRepository
) : ViewModel() {

    val profile: LiveData<Result<ProfileResponse>> get() = userRepository.profile

    fun getProfile(context: Context): LiveData<Result<ProfileResponse>>{
        return userRepository.getProfile(context)
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}

