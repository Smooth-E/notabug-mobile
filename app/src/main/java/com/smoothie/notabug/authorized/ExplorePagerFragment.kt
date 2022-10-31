package com.smoothie.notabug.authorized

import androidx.fragment.app.Fragment
import com.smoothie.notabug.HubActivity
import com.smoothie.notabug.PagerFragment
import com.smoothie.notabug.R
import com.smoothie.notabug.UsersFragment
import com.smoothie.notabug.explore.MirrorsFragment
import com.smoothie.notabug.explore.OrganizationsFragment
import com.smoothie.notabug.explore.RepositoriesFragment

class ExplorePagerFragment(searchQuery: String = "") : PagerFragment(
    arrayOf(
        R.drawable.ic_baseline_class_24,
        R.drawable.ic_baseline_collections_bookmark_24,
        R.drawable.ic_baseline_person_24,
        R.drawable.ic_baseline_groups_24
    ),
    searchQuery
) {

    override fun getInstance(): PagerFragment = this

    override fun createInstance(searchQuery: String): PagerFragment = ExplorePagerFragment(searchQuery)

    override fun getFragment(tabPosition: Int, searchQuery: String): Fragment {
        return when (tabPosition) {
            0 -> RepositoriesFragment(searchQuery)
            1 -> MirrorsFragment(searchQuery)
            2 -> UsersFragment(searchQuery)
            3 -> OrganizationsFragment(searchQuery)
            else -> throw IllegalArgumentException("Unexpected tab position $tabPosition")
        }
    }

}
