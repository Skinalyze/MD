package com.example.skinalyze.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.DetailProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _detailProduct = MutableLiveData<DetailProductResponse>()
    val detailProduct: LiveData<DetailProductResponse> = _detailProduct

    fun loadProduct(context: Context, id: String){
        val client = ApiConfig.getApiService(context).detailProduct(id)
        client.enqueue(object : Callback<DetailProductResponse> {
            override fun onResponse(
                call: Call<DetailProductResponse>, response: Response<DetailProductResponse>
            ) {
                if (response.isSuccessful) {
                    _detailProduct.value = response.body()
                    Log.d("DEBUG DETAIL PRODUCT:", response.body().toString())
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailProductResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })

    }
}