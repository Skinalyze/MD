package com.example.skinalyze.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.response.DetailProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailProduct = MutableLiveData<Result<DetailProductResponse>>()
    val detailProduct: LiveData<Result<DetailProductResponse>> = _detailProduct

    fun loadProduct(context: Context, id: String) {
        _detailProduct.value = Result.Loading
        val client = ApiConfig.getApiService(context).detailProduct(id)
        client.enqueue(object : Callback<DetailProductResponse> {
            override fun onResponse(
                call: Call<DetailProductResponse>, response: Response<DetailProductResponse>
            ) {
                if (response.isSuccessful) {
                    _detailProduct.value = Result.Success(response.body()!!)
                } else {
                    _detailProduct.value = Result.Error(response.message())
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailProductResponse>, t: Throwable) {
                _detailProduct.value = Result.Error(t.message ?: "Unknown error")
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}
