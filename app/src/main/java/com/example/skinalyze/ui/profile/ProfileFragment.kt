package com.example.skinalyze.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.skinalyze.HistoryActivity
import com.example.skinalyze.R
import com.example.skinalyze.SkinTypeActivity
import com.example.skinalyze.Utils.skinTypeTranslate
import com.example.skinalyze.databinding.FragmentProfileBinding
import com.example.skinalyze.viewmodel.ViewModelFactory
import com.example.skinalyze.data.repository.Result


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getProfile().observe(viewLifecycleOwner) {
        }

        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val profile = it.data
                        binding.name.text = profile.nama
                        binding.email.text = profile.email
                        binding.age.text = profile.age.toString()
                        if (profile.gender == 1) {
                            binding.gender.text = getString(R.string.woman)
                        } else {
                            binding.gender.text = getString(R.string.man)
                        }

                        if (profile.sensitif == null && profile.skintype == null) {
                            binding.skinType.text = getString(R.string.no_skintype)
                        } else {
                            profile.skintype?.let {
                                val translatedSkinType = skinTypeTranslate(it)
                                val skinTypeText = if (profile.sensitif == 1) {
                                    "$translatedSkinType ${getString(R.string.sensitive_skin)}"
                                } else {
                                    translatedSkinType
                                }
                                binding.skinType.text = skinTypeText
                            }
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(it.error)
                        Log.d("profile", it.error)
                    }
                }
            }
        }

        binding.editIcon.setOnClickListener {
            val intent = Intent(requireContext(), SkinTypeActivity::class.java)
            startActivity(intent)
        }

        binding.nextIcon.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}