package com.smoothie.notabug.repository

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.smoothie.notabug.R

class RepositoryViewFragment(page: String) : PageParserFragment(page, R.layout.fragment_repository_view) {

    private lateinit var viewGroupTopAppBar: ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewGroupTopAppBar = loadableActivity.findViewById(R.id.top_app_bar)

        viewGroupTopAppBar.setPadding(
            viewGroupTopAppBar.paddingLeft,
            viewGroupTopAppBar.top + loadableActivity.systemBarInsets().top,
            viewGroupTopAppBar.paddingRight,
            viewGroupTopAppBar.paddingBottom
        )
    }

}