package com.example.skinalyze

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skinalyze.databinding.ActivityMainBinding
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.ui.profile.ProfileViewModel
import com.example.skinalyze.viewmodel.MainViewModel
import com.example.skinalyze.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->

            if (!user.isLogin) {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            } else {
                Log.d("DEBUG", "user is logged in with token: ${user.accessToken}")
                profileViewModel.getProfile().observe(this) {
                }
                checkIfSkinTypeIsNull()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_discovery)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_product, R.id.navigation_camera, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupView()
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
        supportActionBar?.hide()
    }

    private fun checkIfSkinTypeIsNull() {
        Log.d("DEBUG", "check skin type")

        profileViewModel.profileResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is Result.Loading -> {
                        Log.d("DEBUG", "is loading")
                        showLoading(true)
                    }
                    is Result.Success -> {
                        Log.d("DEBUG", "success")
                        showLoading(false)
                        val profile = it.data
                        Log.d("DEBUG sensitif", profile.sensitif.toString())
                        Log.d("DEBUG skin type", profile.skintype.toString())

                        if (profile.sensitif == null || profile.skintype == null) {
                            navigateToSkinTypeActivity()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Log.d("DEBUG", "error")
                    }
                }
            }
        }
    }

    private fun navigateToSkinTypeActivity() {
        Log.d("DEBUG", "navigate to skin type")
        val intent = Intent(this@MainActivity, SkinTypeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}