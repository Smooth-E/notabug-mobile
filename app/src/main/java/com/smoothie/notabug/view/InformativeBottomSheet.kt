package com.smoothie.notabug.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsets.Type
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.smoothie.notabug.R

@SuppressLint("PrivateResource")
class InformativeBottomSheet(context: Context) : Dialog(context, R.style.Dialog_NotABugMobile_BottomSheetDialog) {

    private val viewRoot: View
    private val imageViewIcon: ImageView
    private val textViewHeader: TextView
    private val textViewDescription: TextView
    private val progressIndicator: LinearProgressIndicator
    private val navigationBarColor: Int

    private var oldNavigationBarColor: Int

    var icon: Drawable? = null
        set(value) {
            if (value == null) imageViewIcon.visibility = View.GONE
            else {
                imageViewIcon.visibility = View.VISIBLE
                imageViewIcon.setImageDrawable(value)
            }
            field = value
        }

    var header: String? = null
        set(value) {
            applyText(textViewHeader, value)
            field = value
        }

    var description: String? = null
        set(value) {
            applyText(textViewDescription, value)
            field = value
        }

    init {
        setContentView(R.layout.bottom_sheet_informative)
        setCancelable(false)
        viewRoot = findViewById(R.id.view_root)
        imageViewIcon = findViewById(R.id.icon)
        textViewHeader = findViewById(R.id.header_title)
        textViewDescription = findViewById(R.id.description)
        progressIndicator = findViewById(R.id.progress_indicator)
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, typedValue, true)
        navigationBarColor = typedValue.data
        oldNavigationBarColor = window!!.navigationBarColor
    }

    constructor(context: Context, icon: Drawable, header: String, description: String) : this(context)  {
        this.icon = icon
        this.header = header
        this.description = description
    }

    constructor(context: Context, iconResource: Int, headerResource: Int, descriptionResource: Int) : this(
        context,
        ResourcesCompat.getDrawable(context.resources, iconResource, context.theme)!!,
        context.getString(headerResource),
        context.getString(descriptionResource)
    )

    private fun applyText(textView: TextView, text: String?) {
        if (text.isNullOrEmpty()) textView.visibility = View.GONE
        else {
            textView.visibility = View.VISIBLE
            textView.text = text
        }
    }

    override fun show() {
        super.show()
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        oldNavigationBarColor = window!!.navigationBarColor
        window?.navigationBarColor = navigationBarColor
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val paddingBottom = window!!.decorView.rootWindowInsets.stableInsetBottom
        viewRoot.setPadding(0, 0, 0, paddingBottom)
    }

    override fun dismiss() {
        super.dismiss()
        window?.navigationBarColor = oldNavigationBarColor
    }

}
