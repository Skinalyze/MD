package com.example.skinalyze.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.skinalyze.Utils.ImageClassifierHelper
import com.example.skinalyze.databinding.FragmentCameraBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.skinalyze.R
import com.example.skinalyze.ResultActivity
import com.example.skinalyze.Utils.getImageUri
import com.example.skinalyze.Utils.skinProblemToLabel
import com.example.skinalyze.Utils.skinTypeMapping
import com.example.skinalyze.data.repository.Result
import com.example.skinalyze.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.SimpleDateFormat
import java.util.Locale

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?) {
                    requireActivity().runOnUiThread {
                        results?.let {
                            Log.d("camera", it.toString())
                            val skinProblem = it[0].categories[0].label
                            val idSkinProblem = skinProblemToLabel(skinProblem.trim())
                            getUserInfo(idSkinProblem)
                        }
                    }
                }
            }
        )
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
        showLoading(true)
    }

    private fun getUserInfo(idSkinProblem: String) {
        viewModel.getProfile().observe(viewLifecycleOwner) {
        }

        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        val skintype = it.data.skintype
                        val idSkintype = skintype?.let { it1 -> skinTypeMapping(it1.trim()) }

                        if (idSkintype != null) {
                            postRecommendation(idSkintype, idSkinProblem)
                        }
                    }
                    is Result.Error -> {
                        showToast(it.error)
                        Log.d("profile", it.error)
                    }
                }
            }
        }
    }

    private fun postRecommendation(idSkintype: Int, idSkinProblem: String) {
        viewModel.postRecommendation(idSkintype, idSkinProblem).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra(ResultActivity.ID_RESULT, result.data.id_rekomendasi.toString())
                    intent.putExtra(ResultActivity.PREVIOUS_ACTIVITY, "camera")
                    startActivity(intent)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}