package com.smoothie.notabug.anonymous

import androidx.fragment.app.Fragment
import com.smoothie.notabug.R
import org.jsoup.nodes.Element

class MirrorsFragment : CodeRecyclerViewFragment() {

    override var connectionUrl = "https://notabug.org/explore/mirrors"
    override var iconResource = R.drawable.ic_baseline_collections_bookmark_24

}
