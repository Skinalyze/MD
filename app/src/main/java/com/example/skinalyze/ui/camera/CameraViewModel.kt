package com.example.skinalyze.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.ProfileResponse

class CameraViewModel(private val userRepository: UserRepository
) : ViewModel() {

    val profileResult: LiveData<Result<ProfileResponse>> get() = userRepository.profileResult

    fun getProfile(): LiveData<Result<ProfileResponse>>{
        return userRepository.getProfile()
    }

    fun postRecommendation(idSkinType: Int, idSkinProblem: String) = userRepository.postRecommendation(idSkinType, idSkinProblem)
}