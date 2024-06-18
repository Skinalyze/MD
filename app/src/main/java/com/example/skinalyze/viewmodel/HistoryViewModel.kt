package com.example.skinalyze.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.Recommendation

class HistoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    val historyResult: LiveData<Result<List<Recommendation>>> get() = userRepository.historyResult

    fun getHistory(): LiveData<Result<List<Recommendation>>> {
        return userRepository.getHistory()
    }
}