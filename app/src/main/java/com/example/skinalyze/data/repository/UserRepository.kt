package com.example.skinalyze.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.api.ApiService
import com.example.skinalyze.data.request.DeleteRequest
import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RecommendationRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.data.response.DetailRecommendationResponse
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.ProfileResponse
import com.example.skinalyze.data.response.Recommendation
import com.example.skinalyze.data.response.RegisterResponse
import com.example.skinalyze.pref.UserModel
import com.example.skinalyze.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private val _profile = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> get() = _profile

    private val _history = MutableLiveData<Result<List<Recommendation>>>()
    val historyResult: LiveData<Result<List<Recommendation>>> get() = _history

    private val _detailRecommendation = MutableLiveData<Result<DetailRecommendationResponse>>()
    val detailRecommendationResult: LiveData<Result<DetailRecommendationResponse>> get() = _detailRecommendation

    private val _deleteRecommendation = MutableLiveData<Result<RegisterResponse>>()
    val deleteRecommendationResult: LiveData<Result<RegisterResponse>> get() = _deleteRecommendation

    private val _recommendations = MutableLiveData<Result<List<DetailProductResponse>>>()
    val recommendationsResult: LiveData<Result<List<DetailProductResponse>>> get() = _recommendations

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun register(nama: String, email: String, password: String, gender: String, age: Int) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val registerRequest = RegisterRequest(nama, email, password, gender, age)
            val successResponse = apiService.register(registerRequest)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password:String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val loginRequest = LoginRequest(email, password)
            val successResponse = apiService.login(loginRequest)
            emit(Result.Success(successResponse))
            val accessToken = successResponse.accessToken.toString()
            val refreshToken = successResponse.refreshToken.toString()
            val idUser = successResponse.idUser.toString()
            saveSession(UserModel(accessToken, refreshToken, idUser, true))
            Log.d("DEBUG", getSession().toString())
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getProfile() : LiveData<Result<ProfileResponse>> = liveData {
        _profile.value = Result.Loading
        val client = apiService.profile()
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>, response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _profile.value = Result.Success(it)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getRecommendation(idSkinType: Int, idSkinProblem: String) : LiveData<Result<List<DetailProductResponse>>> = liveData {
        _recommendations.value = Result.Loading
        val client = apiService.getRecommendation(idSkinType, idSkinProblem)
        client.enqueue(object : Callback<List<DetailProductResponse>> {
            override fun onResponse(
                call: Call<List<DetailProductResponse>>, response: Response<List<DetailProductResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _recommendations.value = Result.Success(it)
                    }
                } else {
                    _recommendations.value = Result.Error(response.message())
                }
            }
            override fun onFailure(call: Call<List<DetailProductResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun postRecommendation(idSkinType: Int, idSkinProblem: String, idSkinCare: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val recommendationRequest = RecommendationRequest(idSkinType, idSkinProblem, idSkinCare)
            val successResponse = apiService.postRecommendation(recommendationRequest)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getHistory() : LiveData<Result<List<Recommendation>>> = liveData {
        _history.postValue(Result.Loading)
        val client = apiService.getHistory()
        client.enqueue(object : Callback<List<Recommendation>> {
            override fun onResponse(
                call: Call<List<Recommendation>>, response: Response<List<Recommendation>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _history.postValue(Result.Success(it))
                    }
                } else {
                    when (response.code()) {
                        404 -> {
                            _recommendations.postValue(Result.Success(emptyList()))
                        }
                        else -> {
                            _recommendations.postValue(Result.Error("An error occurred: ${response.message()}"))
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Recommendation>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getDetailRecommendation(id: String) : LiveData<Result<DetailRecommendationResponse>> = liveData {
        _detailRecommendation.value = Result.Loading
        val client = apiService.detailRecommendation(id)
        client.enqueue(object : Callback<DetailRecommendationResponse> {
            override fun onResponse(
                call: Call<DetailRecommendationResponse>, response: Response<DetailRecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _detailRecommendation.value = Result.Success(it)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailRecommendationResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun deleteRecommendation(id: Int) : LiveData<Result<RegisterResponse>> = liveData {
        _deleteRecommendation.value = Result.Loading

        val deleteRequest = DeleteRequest(id)
        val client = apiService.deleteRecommendation(deleteRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _deleteRecommendation.value = Result.Success(it)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}