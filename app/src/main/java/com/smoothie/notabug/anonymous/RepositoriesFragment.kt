package com.smoothie.notabug.anonymous

import com.smoothie.notabug.R
import org.jsoup.nodes.Element

class RepositoriesFragment : CodeRecyclerViewFragment() {

    override val connectionUrl = "https://notabug.org/explore/repos"
    override val iconResource = R.drawable.ic_baseline_class_24

}