package com.dream_warriors.hunger4none.presentation.base

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.dream_warriors.hunger4none.R
import com.dream_warriors.hunger4none.presentation.loading.LoadingDialog

abstract class BaseActivity : AppCompatActivity()
{
    private lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        loading = LoadingDialog(this@BaseActivity)
    }

    fun showLoading()
    {
        if (!loading.isShowing)
        {
            loading.show()
        }
    }

    fun dismissLoading()
    {
        if (loading.isShowing)
        {
            loading.dismiss()
        }
    }

    fun showError(error: String)
    {
        dismissLoading()
        MaterialDialog(this@BaseActivity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.bottomsheet_error)
            cornerRadius(16f)
            findViewById<TextView>(R.id.data).text = error
        }
    }
}