package com.example.skinalyze.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.data.response.ProfileResponse
import com.example.skinalyze.data.response.Recommendation

class CameraViewModel(private val userRepository: UserRepository
) : ViewModel() {

    val profileResult: LiveData<Result<ProfileResponse>> get() = userRepository.profileResult

    fun getProfile(): LiveData<Result<ProfileResponse>>{
        return userRepository.getProfile()
    }

    val recommendationsResult: LiveData<Result<List<DetailProductResponse>>> get() = userRepository.recommendationsResult
    fun getRecommendation(idSkinType: Int, idSkinProblem: String): LiveData<Result<List<DetailProductResponse>>> {
        return userRepository.getRecommendation(idSkinType, idSkinProblem)
    }

    fun postRecommendation(idSkinType: Int, idSkinProblem: String, idSkinCare: String) = userRepository.postRecommendation(idSkinType, idSkinProblem, idSkinCare)

    val historyResult: LiveData<Result<List<Recommendation>>> get() = userRepository.historyResult

    fun getHistory(): LiveData<Result<List<Recommendation>>> {
        return userRepository.getHistory()
    }
}