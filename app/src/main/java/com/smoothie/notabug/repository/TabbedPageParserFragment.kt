package com.smoothie.notabug.repository

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smoothie.notabug.R

abstract class TabbedPageParserFragment(
    private val topAppBarLayoutResource: Int,
    private val headerSectionLayoutResource: Int,
    page: String
) : PageParserFragment(page, R.layout.fragment_tabbed_page) {

    private var animationDurationShort: Long = 0

    private var previousVerticalOffset: Int = 0

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var topAppBar: ViewGroup
    private lateinit var header: ViewGroup
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    protected abstract val tabResources: Array<Pair<Int, Int>>
    protected abstract val tabCount: Int

    protected abstract fun getFragment(position: Int): Fragment

    private inner class StateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun createFragment(position: Int): Fragment = getFragment(position)

        override fun getItemCount(): Int = tabCount

    }

    private fun colorView(view: View, colorFrom: Int, colorTo: Int) {
        val animator = ValueAnimator
            .ofObject(ArgbEvaluator(), colorFrom, colorTo)
        animator.duration = animationDurationShort
        animator.addUpdateListener {
            view.background = ColorDrawable(animator.animatedValue as Int)
        }
        animator.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animationDurationShort =
            resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        appBarLayout = view.findViewById(R.id.app_bar_layout)
        topAppBar = view.findViewById(R.id.container_top_app_bar)
        header = view.findViewById(R.id.container_header)
        tabLayout = view.findViewById(R.id.view_tab_layout)
        viewPager2 = view.findViewById(R.id.view_pager2)

        layoutInflater.inflate(topAppBarLayoutResource, topAppBar)
        layoutInflater.inflate(headerSectionLayoutResource, header)

        viewPager2.adapter = StateAdapter(childFragmentManager, lifecycle)
        viewPager2.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            val pair = tabResources[position]
            tab.setIcon(pair.first)
            tab.setText(pair.second)
        }.attach()

        topAppBar.setPadding(
            topAppBar.paddingLeft,
            topAppBar.top + loadableActivity.systemBarInsets().top,
            topAppBar.paddingRight,
            topAppBar.paddingBottom
        )

        val elevatedColor = SurfaceColors.getColorForElevation(loadableActivity, 8f)
        val typedValue = TypedValue()
        requireActivity().theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        val defaultColor = typedValue.data

        header.post {
            val headerHeight = header.measuredHeight
            appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                Log.w("offset", "$verticalOffset")
                if (previousVerticalOffset == 0 && verticalOffset < 0)
                    colorView(topAppBar, defaultColor, elevatedColor)
                else if (verticalOffset == 0)
                    colorView(topAppBar, elevatedColor, defaultColor)
                if (verticalOffset == -headerHeight && previousVerticalOffset > -headerHeight)
                    colorView(tabLayout, defaultColor, elevatedColor)
                else if (verticalOffset > -headerHeight && previousVerticalOffset == -headerHeight)
                    colorView(tabLayout, elevatedColor, defaultColor)
                previousVerticalOffset = verticalOffset
            }
        }
    }

}
