package com.example.skinalyze

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.skinalyze.Utils.skinProblemMapping
import com.example.skinalyze.Utils.skinTypeTranslate
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.databinding.ActivityDetailBinding
import com.example.skinalyze.viewmodel.DetailViewModel
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

        viewModel.detailProduct.observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    showLoading(false)
                    setDetailProductData(result.data)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }

        id?.let { viewModel.loadProduct(this, it) }

        setupView()
        setupActionBar()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            val color = ContextCompat.getColor(this@DetailActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            title = ""
            elevation = 0.0F
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@DetailActivity, R.drawable.baseline_arrow_back_ios_24)
            setHomeAsUpIndicator(upArrow)
        }
    }

    private fun setDetailProductData(product: DetailProductResponse) {
        val listProblem = mutableSetOf<String>()
        for (i in 0 until (product.notableEffects?.size ?: 0)) {
            product.notableEffects?.get(i)?.let { skinProblemMapping(it) }
                ?.let { listProblem.addAll(it) }
        }
        val problems = listProblem.joinToString()

        var skinTypes = product.skinType?.get(0)?.let { skinTypeTranslate(it) }
        for (i in 1 until (product.skinType?.size ?: 1)) {
            skinTypes += ", " + (product.skinType?.get(i)?.let { skinTypeTranslate(it) } ?: "")
        }
        var imageSrc = product.pictureSrc
        if (imageSrc?.endsWith("\r")!!) {
            imageSrc = imageSrc.dropLast(1)
        }

        Glide.with(this)
            .load(imageSrc)
            .error(R.drawable.baseline_broken_image_24)
            .into(binding.imgProduct)

        binding.tvBrandType.text = product.brand
        binding.tvName.text = product.productName
        binding.tvDescription.text = product.description
        binding.tvPrice.text = product.price
        binding.tvProblem.text = problems
        binding.tvSkinType.text = skinTypes
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ID = "id"
    }
}