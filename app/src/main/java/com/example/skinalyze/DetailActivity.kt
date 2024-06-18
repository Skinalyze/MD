package com.example.skinalyze

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions;
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
        val listProblem = mutableSetOf<String>()
        for (i in 0 until product?.notableEffects?.size!!) {
            product.notableEffects[i]?.let { skinProblemMapping(it) }
                ?.let { listProblem.addAll(it) }
        }
        val problems = listProblem.joinToString()

        Log.d("DEBUG PROBLEMS", problems)

        var skinTypes = product.skinType?.get(0)
        for (i in 1 until product.skinType?.size!!) {
            skinTypes += ", " + product.skinType[i]
        }
        var imageSrc = product.pictureSrc
        if (imageSrc?.endsWith("\r")!!) {
            imageSrc = imageSrc.dropLast(1)
        }

        Log.d("cek image", imageSrc)
        Glide.with(this).load(imageSrc).into(binding.imgProduct)

//        Glide.with(binding.imgProduct.context)
//            .load(imageSrc)
////            .override(800, 800)
//            .centerCrop()
//            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
//            .error(android.R.drawable.stat_notify_error)
//            .into(binding.imgProduct)
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