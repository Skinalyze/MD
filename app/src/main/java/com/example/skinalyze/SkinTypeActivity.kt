package com.example.skinalyze

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skinalyze.Utils.Mapper
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.databinding.ActivityLoginBinding
import com.example.skinalyze.databinding.ActivitySkinTypeBinding
import com.example.skinalyze.viewmodel.LoginViewModel
import com.example.skinalyze.viewmodel.SkinTypeViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory

class SkinTypeActivity : AppCompatActivity() {
    private val viewModel by viewModels<SkinTypeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySkinTypeBinding
    private val mapper = Mapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkinTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupAction()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            val color = ContextCompat.getColor(this@SkinTypeActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@SkinTypeActivity, R.drawable.baseline_arrow_back_24)
            setHomeAsUpIndicator(upArrow)
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
                skinTypeId = mapper.getIdFromSkinTypeLabel(skinType)!!
                Log.d("Final Skin Type", skinType)
                Log.d("Final Skin Type ID", skinTypeId)
            }

            val selectedSensitiveSkinId = radioSensitiveSkin.checkedRadioButtonId
            var sensitiveSkinId = "-1"
            if (selectedSensitiveSkinId != -1) {
                val selectedSensitiveSkin: RadioButton = findViewById(selectedSensitiveSkinId)
                val sensitiveSkin = selectedSensitiveSkin.text.toString()
                sensitiveSkinId = mapper.getIdFromSensitiveLabel(sensitiveSkin)!!
                Log.d("Final Sensitive Skin", sensitiveSkin)
                Log.d("Final Sensitive Skin ID", sensitiveSkinId)
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
        Log.d("DEBUG", "navigate to main")
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
}