package com.smoothie.notabug.anonymous

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smoothie.notabug.FadingFragment
import com.smoothie.notabug.R

class PeopleFragment : FadingFragment(R.layout.fragment_anonymous_pager) {

    private val tabNames = arrayOf(R.string.tab_users, R.string.tab_organizations)
    private val tabIcons = arrayOf(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_groups_24)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager2)
        val tabLayout = view.findViewById<TabLayout>(R.id.view_tab_layout)
        viewPager2.adapter = PeopleFragmentStateAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setIcon(tabIcons[position])
            tab.setText(tabNames[position])
        }.attach()
    }
}
