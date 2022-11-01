package com.smoothie.notabug.repository

import android.os.Bundle
import android.view.View
import com.smoothie.notabug.FadingFragment

open class PageParserFragment(page: String, layoutResource: Int) : FadingFragment(layoutResource) {

    protected lateinit var loadableActivity: LoadablePageActivity<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadableActivity = activity as LoadablePageActivity<*>
    }

}
