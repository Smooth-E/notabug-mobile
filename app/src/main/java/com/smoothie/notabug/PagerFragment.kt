package com.smoothie.notabug

import android.os.Bundle
import android.util.Log
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

    protected abstract fun getFragment(tabPosition: Int, searchQuery: String): Fragment

    inner class StateAdapter : FragmentStateAdapter {

        private var searchQuery: String
        private var items = ArrayList<Fragment>()

        constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, searchQuery: String = "") : super(fragmentManager, lifecycle) {
            this.searchQuery = searchQuery
            for (i in tabIconResources.indices) items.add(getFragment(i, ""))
        }

        fun changeQuery(query: String) {
            this.searchQuery = query
            for (fragment in items) fragment.onDestroyView()
            items.clear()
            //notifyItemRangeRemoved(0, itemCount)
            for (i in 0 until itemCount) items.add(getFragment(i, query))
            notifyItemRangeChanged(0, itemCount)
        }

        override fun getItemCount(): Int = tabIconResources.size

        override fun createFragment(position: Int): Fragment {
            Log.d("creteFragment()", "Called with position: $position")
            return items[position]
        }

        override fun getItemId(position: Int): Long = if (position < items.size) items[position].hashCode().toLong() else "NO ID".hashCode().toLong()
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
                (viewPager2.adapter as StateAdapter).changeQuery(query)
            }
        })
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
    }
}
