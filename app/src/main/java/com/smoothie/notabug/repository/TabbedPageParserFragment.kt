package com.smoothie.notabug.repository

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smoothie.notabug.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.math.max
import kotlin.math.min

abstract class TabbedPageParserFragment(
    private val topAppBarLayoutResource: Int,
    private val headerSectionLayoutResource: Int,
    page: String
) : PageParserFragment(page, R.layout.fragment_tabbed_page) {
    
    private lateinit var topAppBarColorAnimator : ValueAnimator
    private lateinit var tabLayoutColorAnimator: ValueAnimator
    private lateinit var viewGroupTopAppBar: ViewGroup
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var header: FrameLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var tabLayoutAlternative: TabLayout
    private lateinit var viewPager2: ViewPager2

    protected abstract val tabResources: Array<Pair<Int, Int>>

    abstract fun getTabCount(): Int

    abstract fun getFragmentAtPosition(position: Int): Fragment

    fun notifyScrollChange(scrollView: NestedScrollView, scrollY: Int) {
        val difference = header.measuredHeight - nestedScrollView.scrollY
        if (scrollY > 0 && difference > 0) {
            nestedScrollView.scrollY =  nestedScrollView.scrollY + scrollY
        }
        else if (scrollY < 0 && nestedScrollView.scrollY > 0) {
            nestedScrollView.scrollY = nestedScrollView.scrollY + scrollY
        }
    }

    private inner class StateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = getTabCount()

        override fun createFragment(position: Int): Fragment = getFragmentAtPosition(position)

    }

    private fun initializeTabLayout(tabLayout: TabLayout, tabLayoutTwin: TabLayout) {
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tabLayout.visibility != View.VISIBLE) return
                tabLayoutTwin.selectTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {  }

            override fun onTabReselected(tab: TabLayout.Tab?) {  }

        })

        tabLayout.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            if (tabLayout.visibility != View.VISIBLE) return@setOnScrollChangeListener
            tabLayoutTwin.scrollX = scrollX
        }
    }

    private fun attachTabLayoutMediator(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            val pair = tabResources[position]
            tab.setIcon(pair.first)
            tab.setText(pair.second)
        }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewGroupTopAppBar = view.findViewById(R.id.container_top_app_bar)
        header = view.findViewById(R.id.container_header)
        tabLayoutAlternative = view.findViewById(R.id.view_tab_layout_alternative)
        nestedScrollView = view.findViewById(R.id.nested_scroll_view)
        tabLayout = view.findViewById(R.id.view_tab_layout)
        viewPager2 = view.findViewById(R.id.view_pager2)

        layoutInflater.inflate(topAppBarLayoutResource, viewGroupTopAppBar)
        layoutInflater.inflate(headerSectionLayoutResource, header)
        
        viewGroupTopAppBar.setPadding(
            viewGroupTopAppBar.paddingLeft,
            viewGroupTopAppBar.top + loadableActivity.systemBarInsets().top,
            viewGroupTopAppBar.paddingRight,
            viewGroupTopAppBar.paddingBottom
        )

        initializeTabLayout(tabLayout, tabLayoutAlternative)
        initializeTabLayout(tabLayoutAlternative, tabLayout)

        val elevatedColor = SurfaceColors.getColorForElevation(loadableActivity, 8f)
        val topAppBarBackgroundColor = (viewGroupTopAppBar.background as ColorDrawable).color
        val animationDurationMedium =
            resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        val animationDurationShort =
            resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        tabLayoutAlternative.background = ColorDrawable(elevatedColor)

        viewPager2.adapter = StateAdapter(childFragmentManager, lifecycle)
        viewPager2.isUserInputEnabled = false
        viewPager2.setCurrentItem(0, false)

        attachTabLayoutMediator(tabLayout)

        nestedScrollView.post {

            val layoutParams = viewPager2.layoutParams
            layoutParams.height = view.measuredHeight - tabLayout.measuredHeight
            viewPager2.layoutParams = layoutParams

            val difference = nestedScrollView.measuredHeight -
                    viewPager2.measuredHeight - tabLayout.measuredHeight

            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                    _, _, scrollY, _, oldScrollY ->
                if (oldScrollY <= 0 && scrollY > 0) {
                    topAppBarColorAnimator = ValueAnimator
                        .ofObject(ArgbEvaluator(), topAppBarBackgroundColor, elevatedColor)
                    topAppBarColorAnimator.duration = animationDurationMedium
                    topAppBarColorAnimator.addUpdateListener {
                        viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int)
                    }
                    topAppBarColorAnimator.start()
                }
                else if (oldScrollY > 0 && scrollY <= 0) {
                    topAppBarColorAnimator = ValueAnimator
                        .ofObject(ArgbEvaluator(), elevatedColor, topAppBarBackgroundColor)
                    topAppBarColorAnimator.duration = animationDurationMedium
                    topAppBarColorAnimator.addUpdateListener {
                        viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int)
                    }
                    topAppBarColorAnimator.start()
                }

                if (difference in (oldScrollY + 1)..scrollY) {
                    tabLayoutColorAnimator = ValueAnimator.ofObject(
                        ArgbEvaluator(),
                        topAppBarBackgroundColor,
                        elevatedColor
                    )
                    tabLayoutColorAnimator.duration = animationDurationShort
                    tabLayoutColorAnimator.addUpdateListener {
                        val drawable = ColorDrawable(it.animatedValue as Int)
                        tabLayout.background = drawable
                        tabLayoutAlternative.background = drawable
                    }
                    tabLayoutColorAnimator.start()
                    tabLayout.visibility = View.INVISIBLE
                    tabLayoutAlternative.visibility = View.VISIBLE
                    attachTabLayoutMediator(tabLayoutAlternative)
                }
                else if (difference in (scrollY + 1)..oldScrollY) {
                    tabLayoutColorAnimator = ValueAnimator
                        .ofObject(ArgbEvaluator(), elevatedColor, topAppBarBackgroundColor)
                    tabLayoutColorAnimator.duration = animationDurationShort
                    tabLayoutColorAnimator.addUpdateListener {
                        val drawable = ColorDrawable(it.animatedValue as Int)
                        tabLayout.background = drawable
                        tabLayoutAlternative.background = drawable
                    }
                    tabLayoutColorAnimator.start()
                    tabLayout.visibility = View.VISIBLE
                    tabLayoutAlternative.visibility = View.GONE
                    attachTabLayoutMediator(tabLayout)
                }
            })  // nestedScrollView.setOnScrollChangeListener

        }  // nestedScrollView.post

    }

}
