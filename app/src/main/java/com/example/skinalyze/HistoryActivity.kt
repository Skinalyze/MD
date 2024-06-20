package com.example.skinalyze

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skinalyze.adapter.RecommendationAdapter
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.data.response.Recommendation
import com.example.skinalyze.databinding.ActivityHistoryBinding
import com.example.skinalyze.viewmodel.HistoryViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noHistory.visibility = View.GONE

        setupView()
        setupActionBar()

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
        adapter = RecommendationAdapter()
        binding.rvHistory.adapter = adapter

        viewModel.getHistory().observe(this) {
        }

        viewModel.historyResult.observe(this) { history ->
            setHistoryData(history)
        }
    }

    private fun setHistoryData(history: Result<List<Recommendation>>) {
        when (history) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                if (history.data.isEmpty()) {
                    binding.noHistory.visibility = View.VISIBLE
                } else {
                    val sortedList = history.data.sortedByDescending { recommendation ->
                        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                        originalFormat.parse(recommendation.timestamp.toString())?.time ?: 0L
                    }

                    adapter.submitList(sortedList)
                    adapter.setOnItemClickCallback(object : RecommendationAdapter.OnItemClickCallback {
                        override fun onItemClicked(id: String) {
                            val resultIntent = Intent(this@HistoryActivity, ResultActivity::class.java)
                            resultIntent.putExtra(ResultActivity.ID_RESULT, id)
                            resultIntent.putExtra(ResultActivity.PREVIOUS_ACTIVITY, "history")
                            startActivity(resultIntent)
                        }
                    })
                }

            }
            is Result.Error -> {
                showLoading(false)
                showToast(history.error)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            val color = ContextCompat.getColor(this@HistoryActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            title = "Riwayat Analisis Kulit"
            elevation = 0.0F
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@HistoryActivity, R.drawable.baseline_arrow_back_ios_24)
            setHomeAsUpIndicator(upArrow)
        }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}