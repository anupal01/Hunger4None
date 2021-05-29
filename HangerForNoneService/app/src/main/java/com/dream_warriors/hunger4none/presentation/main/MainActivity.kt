package com.dream_warriors.hunger4none.presentation.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.expandBottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.dream_warriors.hunger4none.R
import com.dream_warriors.hunger4none.databinding.ActivityMainBinding
import com.dream_warriors.hunger4none.presentation.base.BaseActivity
import com.dream_warriors.hunger4none.utils.Constants
import com.dream_warriors.hunger4none.utils.ImageUrlParser
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : BaseActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null)
        {
            val imagePath = ImageUrlParser.getUriFromExtra(contentResolver, it.data!!.extras!!["data"] as Bitmap)
            Glide.with(this@MainActivity).load(imagePath).thumbnail(0.1f).into(binding.image)
            viewModel.imagePath = imagePath
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null)
        {
            val imagePath = ImageUrlParser.getRealPathFromURI(contentResolver, it.data!!.data!!)
            Glide.with(this@MainActivity).load(imagePath).thumbnail(0.1f).into(binding.image)
            viewModel.imagePath = imagePath
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this@MainActivity, { state -> setView(state) })
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        viewModel.clear()
    }

    private fun init()
    {
        binding.camera.setOnClickListener { openCameraWithPermissionCheck() }

        binding.gallery.setOnClickListener { openGalleryWithPermissionCheck() }

        binding.type.setOnClickListener { selectType() }

        binding.analyse.setOnClickListener { classify() }
    }

    private fun setView(state: MainViewState)
    {
        when (state)
        {
            is MainViewState.LoadingState -> showLoading()
            is MainViewState.DataState -> setData(state.classification, state.accuracy)
            is MainViewState.FeedbackSubmittedState -> dismissLoading()
            is MainViewState.ErrorState -> showError(getString(R.string.general_error))
        }
    }

    private fun setData(classification: String, accuracy: Float)
    {
        dismissLoading()
        MaterialDialog(this@MainActivity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.bottomsheet_results)
            cornerRadius(16f)
            cancelable(false)

            val advice = findViewById<TextView>(R.id.advice)

            findViewById<TextView>(R.id.result).text = getString(R.string.result, classification)
            if (classification.equals(Constants.Category.HEALTHY.name, true))
            {
                advice.visibility = View.GONE
                advice.text = ""
            }
            else
            {
                advice.visibility = View.VISIBLE
                advice.text = getString(R.string.advice)
            }

            findViewById<TextView>(R.id.ok).setOnClickListener {
                dismiss()
                rate(classification, accuracy)
            }
        }
    }

    private fun classify()
    {
        if (viewModel.type != -1 && viewModel.imagePath.isNotEmpty())
        {
            setView(MainViewState.LoadingState)
            viewModel.classify()
        }
        else showError(getString(R.string.missing_data_error))
    }

    private fun submitFeedback(classification: String, accuracy: Float, rate: Int, feedback: String)
    {
        setView(MainViewState.LoadingState)
        viewModel.submitFeedback(classification, accuracy, rate, feedback)
    }

    private fun selectType()
    {
        val dialog = MaterialDialog(this@MainActivity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.bottomsheet_types)
            cornerRadius(16f)

            val list = findViewById<RecyclerView>(R.id.list)
            list.isNestedScrollingEnabled = false
            list.layoutManager = LinearLayoutManager(context)
            val adapter = TypesAdapter { type, index -> setTypeSelection(this, type, index) }
            list.adapter = adapter
            adapter.setData(context.resources.getStringArray(R.array.types).toMutableList())
        }

        Handler(mainLooper).postDelayed({ dialog.expandBottomSheet() }, 300)
    }

    private fun setTypeSelection(dialog: MaterialDialog, type: String, index: Int)
    {
        dialog.dismiss()
        binding.type.text = type
        viewModel.type = index
    }

    private fun rate(classification: String, accuracy: Float)
    {
        MaterialDialog(this@MainActivity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.bottomsheet_rate)
            cornerRadius(16f)

            val rate = findViewById<RatingBar>(R.id.rate)
            val feedback = findViewById<TextInputEditText>(R.id.feedback)

            findViewById<MaterialButton>(R.id.submit).setOnClickListener {
                if (rate.rating.toInt() > 0 && !feedback.text.isNullOrEmpty())
                {
                    dismiss()
                    submitFeedback(classification, accuracy, rate.rating.toInt(), feedback.text.toString())
                }
            }

            findViewById<MaterialButton>(R.id.cancel).setOnClickListener { dismiss() }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openCamera() = cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openGallery() = galleryLauncher.launch(Intent().setType("image/*").setAction(Intent.ACTION_PICK))

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}