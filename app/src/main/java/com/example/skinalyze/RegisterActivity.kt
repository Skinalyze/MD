package com.example.skinalyze

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.skinalyze.databinding.ActivityRegisterBinding
import com.example.skinalyze.viewmodel.RegisterViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory
import com.example.skinalyze.data.repository.Result

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupAction()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            val color = ContextCompat.getColor(this@RegisterActivity, R.color.white)
            setBackgroundDrawable(ColorDrawable(color))
            title = "Daftar"
            setDisplayHomeAsUpEnabled(true)

            val upArrow =
                ContextCompat.getDrawable(this@RegisterActivity, R.drawable.baseline_arrow_back_24)
            setHomeAsUpIndicator(upArrow)
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            showLoading(true)
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val gender = binding.genderEditText.text.toString()
            val age = binding.ageEditText.text.toString().toInt()
            viewModel.register(name, email, password, gender, age)
                .observe(this@RegisterActivity) { result ->
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            showToast("Account created!")
                            navigateToLogin()
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

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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
