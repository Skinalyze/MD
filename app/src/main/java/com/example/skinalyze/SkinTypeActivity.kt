package com.example.skinalyze

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.databinding.ActivitySkinTypeBinding
import com.example.skinalyze.helper.getIdFromSensitiveLabel
import com.example.skinalyze.helper.getIdFromSkinTypeLabel
import com.example.skinalyze.viewmodel.SkinTypeViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory

class SkinTypeActivity : AppCompatActivity() {
    private val viewModel by viewModels<SkinTypeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySkinTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkinTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupActionBar()
        setupAction()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            val color = ContextCompat.getColor(this@SkinTypeActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            title = ""
            elevation = 0.0F
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@SkinTypeActivity, R.drawable.baseline_arrow_back_ios_24)
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

    private fun setupAction() {
        binding.saveButton.setOnClickListener {
            showLoading(true)

            val radioSkinType: RadioGroup = findViewById(R.id.radio_skin_type)
            val radioSensitiveSkin: RadioGroup = findViewById(R.id.radio_sensitive_skin)

            val selectedSkinTypeId = radioSkinType.checkedRadioButtonId
            var skinTypeId = "-1"
            if (selectedSkinTypeId != -1) {
                val selectedSkinType: RadioButton = findViewById(selectedSkinTypeId)
                val skinType = selectedSkinType.text.toString()
                skinTypeId = getIdFromSkinTypeLabel(skinType)
            }

            val selectedSensitiveSkinId = radioSensitiveSkin.checkedRadioButtonId
            var sensitiveSkinId = "-1"
            if (selectedSensitiveSkinId != -1) {
                val selectedSensitiveSkin: RadioButton = findViewById(selectedSensitiveSkinId)
                val sensitiveSkin = selectedSensitiveSkin.text.toString()
                sensitiveSkinId = getIdFromSensitiveLabel(sensitiveSkin)
            }

            viewModel.saveSkinType(skinTypeId, sensitiveSkinId, this).observe(this@SkinTypeActivity) { result ->
                when(result) {
                    is Result.Success -> {
                        showLoading(false)
                        showToast("Berhasil menyimpan!")
                        navigateToMain()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        showToast(result.error)
                        navigateToMain()
                    }

                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@SkinTypeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}