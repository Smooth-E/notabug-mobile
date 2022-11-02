package com.smoothie.notabug.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import com.smoothie.notabug.R

class ToggleableNestedScrollView : NestedScrollView {

    var scrollable = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            val obtainedAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ToggleableNestedScrollView,
                0,
                0)
            scrollable = obtainedAttributes
                .getBoolean(R.styleable.ToggleableNestedScrollView_scrollable, true)
        }
    }

    override fun onTouchEvent(ev: MotionEvent) = super.onTouchEvent(ev) && scrollable

    override fun onInterceptTouchEvent(ev: MotionEvent) =
        super.onInterceptTouchEvent(ev) && scrollable

}
