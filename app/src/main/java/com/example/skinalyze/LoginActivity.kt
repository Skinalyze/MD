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
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.databinding.ActivityLoginBinding
import com.example.skinalyze.viewmodel.LoginViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupActionBar()
        setupAction()
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
            val color = ContextCompat.getColor(this@LoginActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            title = "Masuk"
            elevation = 0.0F
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@LoginActivity, R.drawable.baseline_arrow_back_ios_24)
            setHomeAsUpIndicator(upArrow)
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            showLoading(true)

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when(result) {
                    is Result.Success -> {
                        showLoading(false)
                        showToast("Login berhasil!")
                        navigateToMain()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        showToast(result.error)
                    }

                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
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