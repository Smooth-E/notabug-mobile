package com.smoothie.notabug.repository

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.smoothie.notabug.R
import com.smoothie.notabug.view.ToggleableNestedScrollView

class TabbedPageTabFragment(
    private val parentFragment: TabbedPageParserFragment,
    private val layoutResource: Int)
    : Fragment(R.layout.fragment_tabbed_page_tab) {

    private lateinit var toggleableNestedScrollView: ToggleableNestedScrollView

    var scrollable: Boolean = true
        set (value) {
            toggleableNestedScrollView.scrollable = value
            field = value
        }
        get () = toggleableNestedScrollView.scrollable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleableNestedScrollView = view.findViewById(R.id.toggleable_scroll_view)
        layoutInflater.inflate(layoutResource, toggleableNestedScrollView, true)
        toggleableNestedScrollView.setOnScrollChangeListener (
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.w("", "" + (scrollY - oldScrollY))
                parentFragment.notifyScrollChange(toggleableNestedScrollView, scrollY - oldScrollY)
            }
        )
    }

}