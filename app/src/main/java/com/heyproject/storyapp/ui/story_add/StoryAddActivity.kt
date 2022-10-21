package com.heyproject.storyapp.ui.story_add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.ActivityStoryAddBinding
import com.heyproject.storyapp.ui.SharedViewModel
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.Result
import com.heyproject.storyapp.util.reduceFileImage
import com.heyproject.storyapp.util.rotateBitmap
import com.heyproject.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryAddBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: StoryAddViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var getFile: File? = null
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestPermission()

        binding.apply {
            storyAddActivity = this@StoryAddActivity
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.switchLoc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCurrentLocation()
            } else {
                this.location = null
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.failed_get_persmission), Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    this.location = currentLocation
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.please_activate_location_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.switchLoc.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getCurrentLocation()
                }
                else -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.location_permission_denied),
                        Snackbar.LENGTH_SHORT
                    ).setAction(getString(R.string.location_permission_denied_action)) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }.show()
                    binding.switchLoc.isChecked = false
                }
            }
        }

    private fun requestPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
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
                BitmapFactory.decodeFile(getFile?.path), isBackCamera
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
        val token = sharedViewModel.user.value?.token ?: ""
        val file = reduceFileImage(getFile!!)
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        val description =
            binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
        var lat: RequestBody? = null
        var lon: RequestBody? = null

        if (location?.latitude != null || location?.longitude != null) {
            lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
            lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
        }
        if (formValidation()) {
            viewModel.uploadImage(
                token, imageMultipart, description, lat, lon
            ).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> {
                        setLoading(false)
                        Snackbar.make(
                            binding.root, getString(R.string.upload_success), Snackbar.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    is Result.Error -> {
                        setLoading(false)
                        Snackbar.make(binding.root, getString(R.string.oops), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
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
                this, getString(R.string.choose_image_warn), Toast.LENGTH_SHORT
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