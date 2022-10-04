package com.smoothie.notabug.view

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.smoothie.notabug.R

class InformativeBottomSheet() : BottomSheetDialogFragment() {

    companion object {
        val tag = "Informative Bottom Sheet"
    }

    private lateinit var imageViewIcon: ImageView
    private lateinit var textViewHeader: TextView
    private lateinit var textViewSupportive: TextView
    private lateinit var progressIndicator: LinearProgressIndicator

    var iconResource: Int? = null
        set(value) {
            if (this::imageViewIcon.isInitialized) {
                if (value == null) imageViewIcon.visibility = View.GONE
                else {
                    imageViewIcon.visibility = View.VISIBLE
                    imageViewIcon.setImageResource(value)
                }
            }
            field = value
        }

    var headerText: String? = null
        set(value) {
            if (this::textViewHeader.isInitialized) {
                if (value.isNullOrEmpty()) textViewHeader.visibility = View.GONE
                else {
                    textViewHeader.visibility = View.VISIBLE
                    textViewHeader.text = value
                }
            }
            field = value
        }

    var supportiveText: String? = null
        set(value) {
            if (this::textViewSupportive.isInitialized) {
                if (value.isNullOrEmpty()) textViewSupportive.visibility = View.GONE
                else {
                    textViewSupportive.visibility = View.VISIBLE
                    textViewSupportive.text = value
                }
            }
            field = value
        }

    constructor(iconResource: Int, headerResource: Int, supportiveResource: Int) : this() {
        this.iconResource = iconResource
        this.headerText = getString(headerResource)
        this.supportiveText = getString(supportiveResource)
    }

    fun getProgressIndicator() = progressIndicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_informative, container, false)
        imageViewIcon = view.findViewById(R.id.icon)
        textViewHeader = view.findViewById(R.id.header_title)
        textViewSupportive = view.findViewById(R.id.supportive_text)
        progressIndicator = view.findViewById(R.id.progress_indicator)
        // These will put resources into views
        iconResource = iconResource
        headerText = headerText
        supportiveText = supportiveText
        return view
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}