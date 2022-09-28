package com.smoothie.notabug.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R

class PillSearchBarView : FrameLayout {

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        buildView(context, attrs)
    }

    constructor(context : Context) : super(context) {
        buildView(context, null)
    }

    private fun buildView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.view_pill_search_bar, this)
        findViewById<View>(R.id.view_root).backgroundTintList = ColorStateList.valueOf(SurfaceColors.getColorForElevation(context, 8f))
        if (attrs != null) {
            val obtainedAttributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PillSearchBarView, 0, 0)
            var hint = obtainedAttributes.getString(R.styleable.PillSearchBarView_hint)
            if (hint == null || hint.isEmpty()) hint = context.getString(R.string.label_search)
            val icon = obtainedAttributes.getResourceId(R.styleable.PillSearchBarView_icon, R.drawable.ic_baseline_search_24)
            findViewById<EditText>(R.id.edit_text).hint = hint;
            findViewById<ImageView>(R.id.icon).setImageResource(icon)
        }
    }
}