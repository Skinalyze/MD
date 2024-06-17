package com.example.skinalyze

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.skinalyze.Utils.skinProblemMapping
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.databinding.ActivityDetailBinding
import com.example.skinalyze.viewmodel.DetailViewModel
import com.example.skinalyze.viewmodel.LoginViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(ID)

        viewModel.detailProduct.observe(this) { response ->
            setDetailProductData(response)
        }

        id?.let { viewModel.loadProduct(this, it) }
    }

    private fun setDetailProductData(product: DetailProductResponse?) {
        var problems = product?.notableEffects?.get(0)?.let { skinProblemMapping(it) }
        for (i in 1 until product?.notableEffects?.size!!) {
            problems += ", " + product.notableEffects[i]?.let { skinProblemMapping(it) }
        }

        var skinTypes = product.skinType?.get(0)
        for (i in 1 until product.skinType?.size!!) {
            skinTypes += ", " + product.skinType[i]
        }

        Glide.with(binding.imgProduct.context).load(product.pictureSrc).into(binding.imgProduct)
        binding.tvBrandType.text= product.brand
        binding.tvName.text = product.productName
        binding.tvDescription.text = product.description
        binding.tvPrice.text = product.price
        binding.tvProblem.text = problems
        binding.tvSkinType.text = skinTypes
    }


    companion object {
        const val ID = "id"
    }
}