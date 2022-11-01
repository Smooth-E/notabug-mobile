package com.smoothie.notabug.anonymous

import androidx.fragment.app.Fragment
import com.smoothie.notabug.PagerFragment
import com.smoothie.notabug.R
import com.smoothie.notabug.explore.MirrorsFragment
import com.smoothie.notabug.explore.RepositoriesFragment

class CodeFragment(searchQuery: String = "", selectedTabIndex: Int = 0) : PagerFragment(
    arrayOf(R.drawable.ic_baseline_class_24, R.drawable.ic_baseline_collections_bookmark_24),
    arrayOf(R.string.tab_repositories, R.string.tab_mirrors),
    searchQuery,
    selectedTabIndex
) {

    override fun createInstance(searchQuery: String, selectedTabIndex: Int): PagerFragment =
        CodeFragment(searchQuery, selectedTabIndex)

    override fun getInstance(): PagerFragment = this

    override fun getFragment(tabPosition: Int, searchQuery: String): Fragment {
        return when(tabPosition) {
            0 -> RepositoriesFragment(searchQuery)
            1 -> MirrorsFragment(searchQuery)
            else -> throw IllegalArgumentException("Unexpected tab position $tabPosition")
        }
    }

}
