package com.smoothie.notabug.anonymous

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smoothie.notabug.R

class CodeFragment : Fragment(R.layout.fragment_anonymous_code) {

    private val tabNames = arrayOf(R.string.tab_repositories, R.string.tab_mirrors)
    private val tabIcons = arrayOf(R.drawable.ic_baseline_class_24, R.drawable.ic_baseline_collections_bookmark_24)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager2);
        val tabLayout = view.findViewById<TabLayout>(R.id.view_tab_layout)
        val adapter = CodeFragmentStateAdapter(this)
        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(tabNames[position])
            tab.setIcon(tabIcons[position])
        }.attach()
    }
}