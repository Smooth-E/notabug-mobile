package com.smoothie.notabug.anonymous

import androidx.fragment.app.Fragment
import com.smoothie.notabug.PagerFragment
import com.smoothie.notabug.R
import com.smoothie.notabug.UsersFragment
import com.smoothie.notabug.explore.OrganizationsFragment

class PeopleFragment(searchQuery: String = "", selectedTabIndex: Int = 0) : PagerFragment(
    arrayOf(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_groups_24),
    arrayOf(R.string.tab_users, R.string.tab_organizations),
    searchQuery
) {

    override fun createInstance(searchQuery: String, selectedTabIndex: Int): PagerFragment =
        PeopleFragment(searchQuery, selectedTabIndex)

    override fun getInstance(): PagerFragment = this

    override fun getFragment(tabPosition: Int, searchQuery: String): Fragment {
        return when(tabPosition) {
            0 -> UsersFragment(searchQuery)
            1 -> OrganizationsFragment(searchQuery)
            else -> throw IllegalArgumentException("Unexpected tab position $tabPosition")
        }
    }

}
