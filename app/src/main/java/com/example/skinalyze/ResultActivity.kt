package com.example.skinalyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.skinalyze.Utils.labelToSkinProblem
import com.example.skinalyze.Utils.skinTypeTranslate
import com.example.skinalyze.adapter.RecommendationAdapter
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.databinding.ActivityResultBinding
import com.example.skinalyze.viewmodel.HistoryViewModel
import com.example.skinalyze.viewmodel.ResultViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(ID_RESULT)

        if (id != null) {
            viewModel.getDetailRecommendation(id).observe(this) { response ->
                Log.d("result", response.toString())
            }

            viewModel.detailRecommendationResult.observe(this) { result ->
                Log.d("objek result", result.toString())
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val detail = result.data
                        Log.d("result jadi", detail.toString())
                        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                        originalFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val targetFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))

                        val date: Date? = originalFormat.parse(detail.timestamp)
                        val formattedDate: String = date?.let { targetFormat.format(it) }.orEmpty()

                        binding.tvTimestamp.text = formattedDate

                        var skinType = ""
                        for (type in detail.skinType) {
                            skinType += skinTypeTranslate(type)
                            skinType += ", "
                        }
                        skinType = skinType.dropLast(2)
                        binding.skinType.text = skinType

                        var skinProblem = ""
                        for (problem in detail.skinProblem) {
                            skinProblem += problem
                            skinProblem += ", "
                        }
                        skinProblem = skinProblem.dropLast(2)
                        binding.skinProblem.text = labelToSkinProblem(skinProblem)

                        val nameTextViews = arrayOf(
                            binding.tvItemName1, binding.tvItemName2, binding.tvItemName3,
                            binding.tvItemName4, binding.tvItemName5, binding.tvItemName6,
                            binding.tvItemName10, binding.tvItemName11, binding.tvItemName12,
                            binding.tvItemName7, binding.tvItemName8, binding.tvItemName9,
                            binding.tvItemName13, binding.tvItemName14, binding.tvItemName15
                        )

                        val brandTextViews = arrayOf(
                            binding.tvItemBrand1, binding.tvItemBrand2, binding.tvItemBrand3,
                            binding.tvItemBrand4, binding.tvItemBrand5, binding.tvItemBrand6,
                            binding.tvItemBrand10, binding.tvItemBrand11, binding.tvItemBrand12,
                            binding.tvItemBrand7, binding.tvItemBrand8, binding.tvItemBrand9,
                            binding.tvItemBrand13, binding.tvItemBrand14, binding.tvItemBrand15
                        )

                        val photoImageViews = arrayOf(
                            binding.imgItemPhoto1, binding.imgItemPhoto2, binding.imgItemPhoto3,
                            binding.imgItemPhoto4, binding.imgItemPhoto5, binding.imgItemPhoto6,
                            binding.imgItemPhoto10, binding.imgItemPhoto11, binding.imgItemPhoto12,
                            binding.imgItemPhoto7, binding.imgItemPhoto8, binding.imgItemPhoto9,
                            binding.imgItemPhoto13, binding.imgItemPhoto14, binding.imgItemPhoto15
                        )

                        val cardViews = arrayOf(
                            binding.cardView1, binding.cardView2, binding.cardView3,
                            binding.cardView4, binding.cardView5, binding.cardView6,
                            binding.cardView10, binding.cardView11, binding.cardView12,
                            binding.cardView7, binding.cardView8, binding.cardView9,
                            binding.cardView13, binding.cardView14, binding.cardView15
                        )

                        // Loop through the products list and bind data to corresponding views
                        for (i in detail.skinCare.indices) {
                            val product = detail.skinCare[i]
                            nameTextViews[i].text = product.productName
                            brandTextViews[i].text = product.brand
                            var imgLink = product.img
                            if (imgLink.endsWith("\r")) {
                                imgLink = imgLink.dropLast(1)
                            }
                            Glide.with(this).load(imgLink).into(photoImageViews[i])
                            cardViews[i].setOnClickListener {
                                val moveToDetailUserIntent = Intent(this, DetailActivity::class.java)
                                moveToDetailUserIntent.putExtra(DetailActivity.ID, product.idSkinCare)
                                startActivity(moveToDetailUserIntent)
                            }

                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                        Log.d("result", result.error)
                    }
                }
            }

            binding.deleteIcon.setOnClickListener {
                Log.d("halo", "tes delete")
                viewModel.deleteRecommendation(id.toInt()).observe(this) {

                }

            }



        }


        viewModel.deleteRecommendationResult.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    // TODO redirect

                    Log.d("bisa", it.toString())

                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(it.error)
                    Log.d("error", it.error)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ID_RESULT = "id_result"
    }
}