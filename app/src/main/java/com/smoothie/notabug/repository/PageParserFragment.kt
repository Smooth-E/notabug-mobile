package com.smoothie.notabug.repository

import android.os.Bundle
import android.view.View
import com.smoothie.notabug.FadingFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

open class PageParserFragment(page: String, layoutResource: Int) : FadingFragment(layoutResource) {

    protected val document: Document = Jsoup.parse(page)
    protected lateinit var loadableActivity: LoadablePageActivity<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadableActivity = activity as LoadablePageActivity<*>
    }

}
