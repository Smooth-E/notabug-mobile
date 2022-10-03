package com.smoothie.notabug

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

abstract class PagerFragment(
    private val tabIconResources: Array<Int>,
    private val tabNameResources: Array<Int>
) : FadingFragment(R.layout.fragment_pager) {

    protected abstract fun getFragment(tabPosition: Int): Fragment

    inner class StateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = tabIconResources.size

        override fun createFragment(position: Int) = getFragment(position)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        val viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager2);
        val tabLayout = view.findViewById<TabLayout>(R.id.view_tab_layout)
        val adapter = StateAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter
        viewPager2.isUserInputEnabled = false
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(tabNameResources[position])
            tab.setIcon(tabIconResources[position])
        }.attach()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
    }
}
