package com.smoothie.notabug

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smoothie.notabug.view.PillSearchBarView

abstract class PagerFragment : FadingFragment {

    private val hasNames: Boolean
    private var tabIconResources: Array<Int>
    private lateinit var tabNameResources: Array<Int>


    constructor(tabIconResources: Array<Int>, tabNameResources: Array<Int>) : super(R.layout.fragment_pager) {
        this.hasNames = true
        this.tabIconResources = tabIconResources.clone()
        this.tabNameResources = tabNameResources.clone()
    }

    constructor(tabIconResources: Array<Int>) : super(R.layout.fragment_pager) {
        this.hasNames = false
        this.tabIconResources = tabIconResources.clone()
    }

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
        tabLayout.tabMode = TabLayout.MODE_AUTO
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            if (hasNames) tab.setText(tabNameResources[position])
            tab.setIcon(tabIconResources[position])
        }.attach()
        val pillSearchBarView = view.findViewById<PillSearchBarView>(R.id.pill_search_bar_view)
        pillSearchBarView.setOnSearchListener(object:PillSearchBarView.PillSearchExecuteListener {
            override fun performSearch(query: String) {
                Log.d("TAG", "Reloading with: $query")
                for (i in tabIconResources.indices) {
                    val fragment = getFragment(i)
                    if (fragment is ScrollerRecyclerViewFragment<*, *>) fragment.reloadWithQuery(query)
                }
            }
        })
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
    }
}
