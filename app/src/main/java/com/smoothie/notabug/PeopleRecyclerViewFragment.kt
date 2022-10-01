package com.smoothie.notabug

import com.smoothie.notabug.FadingFragment
import com.smoothie.notabug.R

class PeopleRecyclerViewFragment : FadingFragment(R.layout.fragment_anonymous_scroller) {

    fun loadNewPage() {
        throw NotImplementedError()
    }

    fun getPageNumber() = 0

}