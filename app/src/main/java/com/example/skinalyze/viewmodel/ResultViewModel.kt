package com.example.skinalyze.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.DetailRecommendationResponse
import com.example.skinalyze.data.response.RegisterResponse

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {

    val detailRecommendationResult: LiveData<Result<DetailRecommendationResponse>> get() = userRepository.detailRecommendationResult

    fun getDetailRecommendation(id: String): LiveData<Result<DetailRecommendationResponse>> {
        return userRepository.getDetailRecommendation(id)
    }

    val deleteRecommendationResult: LiveData<Result<RegisterResponse>> get() = userRepository.deleteRecommendationResult

    fun deleteRecommendation(id: Int): LiveData<Result<RegisterResponse>> {
        return userRepository.deleteRecommendation(id)
    }

}