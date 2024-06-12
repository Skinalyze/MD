package com.example.skinalyze.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinalyze.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is notifications Fragment"
//    }
//    val text: LiveData<String> = _text
