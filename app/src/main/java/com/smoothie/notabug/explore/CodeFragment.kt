package com.smoothie.notabug.explore

import androidx.fragment.app.Fragment
import com.smoothie.notabug.PagerFragment
import com.smoothie.notabug.R

class CodeFragment : PagerFragment(
    arrayOf(R.drawable.ic_baseline_class_24, R.drawable.ic_baseline_collections_bookmark_24),
    arrayOf(R.string.tab_repositories, R.string.tab_mirrors)
) {
    override fun getFragment(tabPosition: Int): Fragment {
        return when(tabPosition) {
            0 -> RepositoriesFragment()
            1 -> MirrorsFragment()
            else -> throw IllegalArgumentException("Unexpected tab position $tabPosition")
        }
    }
}