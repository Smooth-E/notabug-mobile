package com.smoothie.notabug.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    var hint : String? = "No hint provided"
        set(value) {
            field = value
            findViewById<EditText>(R.id.edit_text).hint = field
        }

    private lateinit var icon: ImageView
    private lateinit var editText: EditText
    private lateinit var buttonExecute: ImageView
    private lateinit var buttonClear: ImageView

    private var lastQuery = ""

    constructor(context : Context) : super(context) {
        buildView(context, null)
    }

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        buildView(context, attrs)
    }

    private fun buildView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.view_pill_search_bar, this)
        findViewById<View>(R.id.view_root).backgroundTintList =
            ColorStateList.valueOf(SurfaceColors.getColorForElevation(context, 8f))

        icon = findViewById(R.id.icon)
        editText = findViewById(R.id.edit_text)
        buttonExecute = findViewById(R.id.button_execute)
        buttonClear = findViewById(R.id.button_clear)

        buttonExecute.visibility = View.INVISIBLE
        editText.addTextChangedListener { text ->
            if (text != null && text.toString() != lastQuery) {
                if (editText.hasFocus()) {
                    buttonExecute.visibility = VISIBLE
                    buttonClear.visibility = INVISIBLE
                }
                else {
                    buttonExecute.visibility = INVISIBLE
                    buttonClear.visibility = VISIBLE
                }
            }
            else {
                buttonExecute.visibility = INVISIBLE
                buttonClear.visibility = INVISIBLE
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
    }

    fun setOnSearchListener(listener: PillSearchExecuteListener) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                performSearchWithListener(listener)
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }
        }
        buttonExecute.setOnClickListener { performSearchWithListener(listener) }
        buttonClear.setOnClickListener {
            editText.setText("")
            performSearchWithListener(listener)
        }
    }

    fun setText(text: String) {
        editText.setText(text)
        lastQuery = text
    }

}
