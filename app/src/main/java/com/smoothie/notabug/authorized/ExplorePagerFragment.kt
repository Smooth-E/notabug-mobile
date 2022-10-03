package com.smoothie.notabug.authorized

import androidx.fragment.app.Fragment
import com.smoothie.notabug.PagerFragment
import com.smoothie.notabug.R
import com.smoothie.notabug.UsersFragment
import com.smoothie.notabug.explore.MirrorsFragment
import com.smoothie.notabug.explore.OrganizationsFragment
import com.smoothie.notabug.explore.RepositoriesFragment

class ExplorePagerFragment : PagerFragment(
    arrayOf(
        R.drawable.ic_baseline_class_24,
        R.drawable.ic_baseline_collections_bookmark_24,
        R.drawable.ic_baseline_person_24,
        R.drawable.ic_baseline_groups_24
    ),
    arrayOf(
        R.string.tab_repositories,
        R.string.tab_mirrors,
        R.string.tab_users,
        R.string.tab_organizations
    )) {
    override fun getFragment(tabPosition: Int): Fragment {
        return when (tabPosition) {
            0 -> RepositoriesFragment()
            1 -> MirrorsFragment()
            2 -> UsersFragment()
            3 -> OrganizationsFragment()
            else -> throw IllegalArgumentException("Unexpected tab position $tabPosition")
        }
    }
}
