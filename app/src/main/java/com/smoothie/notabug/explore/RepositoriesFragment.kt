package com.smoothie.notabug.explore

import com.smoothie.notabug.R

class RepositoriesFragment(searchQuery: String) : CodeRecyclerViewFragment(searchQuery) {

    override val connectionUrl = "https://notabug.org/explore/repos"

}
