package com.example.skinalyze.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.skinalyze.ResultActivity
import com.example.skinalyze.SkinTypeActivity
import com.example.skinalyze.databinding.FragmentProfileBinding
import com.example.skinalyze.viewmodel.ViewModelFactory

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

        binding.editIcon.setOnClickListener {
            val intent = Intent(requireContext(), SkinTypeActivity::class.java)
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
}