package com.smoothie.notabug.explore

import com.smoothie.notabug.R

class MirrorsFragment(searchQuery: String) : CodeRecyclerViewFragment(searchQuery) {

    override var connectionUrl = "https://notabug.org/explore/mirrors"
    override var iconResource = R.drawable.ic_baseline_collections_bookmark_24

}
