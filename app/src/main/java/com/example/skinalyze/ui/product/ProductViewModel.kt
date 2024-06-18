package com.example.skinalyze.ui.product

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.pref.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> = _listProduct

    lateinit var productName: String

    fun findProduct(context: Context) {
        val client = ApiConfig.getApiService(context).search(productName)

        client.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>, response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    _listProduct.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
    }
}