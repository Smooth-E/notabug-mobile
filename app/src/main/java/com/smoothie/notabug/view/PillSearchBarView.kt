package com.smoothie.notabug.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R

class PillSearchBarView : FrameLayout {

    interface PillSearchExecuteListener {
        fun performSearch(query: String)
    }

    interface PillSearchCancelListener {
        fun cancelSearch()
    }

    var hint : String? = "No hint provided"
        set(value) {
            field = value
            findViewById<EditText>(R.id.edit_text).hint = field
        }

    private lateinit var icon: ImageView
    private lateinit var editText: EditText
    private lateinit var button: ImageView
    private lateinit var cancelSearchListener: PillSearchCancelListener

    private var lastQuery = ""

    constructor(context : Context) : super(context) {
        buildView(context, null)
    }

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        buildView(context, attrs)
    }

    private fun buildView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.view_pill_search_bar, this)
        findViewById<View>(R.id.view_root).backgroundTintList = ColorStateList.valueOf(SurfaceColors.getColorForElevation(context, 8f))

        icon = findViewById(R.id.icon)
        editText = findViewById(R.id.edit_text)
        button = findViewById(R.id.button)

        button.visibility = View.GONE
        editText.addTextChangedListener { text ->
            if (text != null && text.isNotEmpty()) {
                button.setImageResource(R.drawable.ic_round_chevron_right_24)
                button.visibility = View.VISIBLE
            }
            else {
                button.visibility = View.GONE
            }
        }
        cancelSearchListener = object :PillSearchCancelListener {
            override fun cancelSearch() {
                editText.setText("")
                editText.clearFocus()
            }
        }

        if (attrs != null) {
            val obtainedAttributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PillSearchBarView, 0, 0)
            hint = obtainedAttributes.getString(R.styleable.PillSearchBarView_hint)
            if (hint == null || hint!!.isEmpty()) hint = context.getString(R.string.label_search)
            val iconResource = obtainedAttributes.getResourceId(R.styleable.PillSearchBarView_icon, R.drawable.ic_baseline_search_24)
            editText.hint = hint;
            icon.setImageResource(iconResource)
        }
    }

    private fun performSearchWithListener(listener: PillSearchExecuteListener) {
        val text = editText.text.toString()
        if (text == lastQuery) return
        lastQuery = text
        listener.performSearch(text)
        if (text.isNotEmpty()) {
            button.visibility = View.VISIBLE
            button.setOnClickListener { cancelSearchListener.cancelSearch() }
            button.setImageResource(R.drawable.ic_round_cancel_24)
            editText.clearFocus()
        }
        else {
            button.visibility = View.GONE
            setOnSearchListener(listener)
        }
    }

    fun setOnSearchListener(listener: PillSearchExecuteListener) {
        editText.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) performSearchWithListener(listener) }
        button.setOnClickListener { performSearchWithListener(listener) }
    }
}
