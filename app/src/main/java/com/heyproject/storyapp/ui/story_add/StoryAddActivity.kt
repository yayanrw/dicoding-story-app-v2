package com.heyproject.storyapp.ui.story_add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.ActivityStoryAddBinding
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.model.dataStore
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.RequestState
import com.heyproject.storyapp.util.rotateBitmap
import com.heyproject.storyapp.util.uriToFile
import java.io.File

class StoryAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryAddBinding
    private lateinit var userPreference: UserPreference
    private val viewModel: StoryAddViewModel by viewModels {
        ViewModelFactory(userPreference)
    }
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(dataStore)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.apply {
            storyAddActivity = this@StoryAddActivity
        }

        viewModel.getUser().observe(this) {
            if (!it.isLogin) {
                finish()
            }
        }

        viewModel.requestState.observe(this) {
            when (it) {
                RequestState.LOADING -> {
                    setLoading(true)
                }
                RequestState.ERROR -> {
                    setLoading(false)
                    Snackbar.make(binding.root, getString(R.string.oops), Snackbar.LENGTH_SHORT).show()
                }
                RequestState.NO_CONNECTION -> {
                    setLoading(false)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.no_connection),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    setLoading(false)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.upload_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.linearProgressIndication.visibility = View.VISIBLE
            binding.buttonAdd.isEnabled = false
            binding.btnCamera.isEnabled = false
            binding.btnGallery.isEnabled = false
        } else {
            binding.linearProgressIndication.visibility = View.GONE
            binding.buttonAdd.isEnabled = true
            binding.btnCamera.isEnabled = true
            binding.btnGallery.isEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.failed_get_persmission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ivPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

    fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    fun uploadImage() {
        if (formValidation()) {
            viewModel.uploadImage(getFile!!, binding.edAddDescription.text.toString())
        }
    }

    private fun formValidation(): Boolean {
        var isValid = true
        if (binding.edAddDescription.text.isNullOrEmpty()) {
            isValid = false
            binding.edAddDescription.error = getString(R.string.required)
        } else {
            binding.edAddDescription.error = null
        }

        if (getFile == null) {
            isValid = false
            Toast.makeText(
                this,
                getString(R.string.choose_image_warn),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}