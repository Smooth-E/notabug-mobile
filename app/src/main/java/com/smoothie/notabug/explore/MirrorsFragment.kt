package com.smoothie.notabug.explore

import com.smoothie.notabug.R

class MirrorsFragment(searchQuery: String) : CodeRecyclerViewFragment(searchQuery) {

    override var connectionUrl = "https://notabug.org/explore/mirrors"

}
