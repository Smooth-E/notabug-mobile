package com.smoothie.notabug.explore

import android.view.View
import com.smoothie.notabug.R
import com.smoothie.notabug.ScrollerRecyclerViewFragment
import com.smoothie.notabug.Utilities
import org.jsoup.Jsoup

open class CodeRecyclerViewFragment(searchQuery: String) :
    ScrollerRecyclerViewFragment<CodeRecyclerViewAdapter, CodeRecyclerViewAdapter.DataHolder>(20, searchQuery) {

    protected open val iconResource = R.drawable.ic_baseline_class_24

    override fun getAdapter(): CodeRecyclerViewAdapter = CodeRecyclerViewAdapter(this, data)

    override fun loadNewPage(isReloading: Boolean) = LoadingThread(isReloading).start()

    override fun onDestroyView() {
        super.onDestroyView()
        thread?.interrupt()
    }

    inner class LoadingThread(private val isReloading: Boolean = false) : Thread() {
        override fun run() {
            try {
                thread?.join()
                thread = this
                if (isReloading) {
                    val size = data.size
                    activity?.runOnUiThread {
                        recyclerView.visibility = View.VISIBLE
                        for (index in 0 until size) data.removeFirst()
                        recyclerView.adapter?.notifyItemRangeRemoved(0, size)
                    }
                }
                pageNumber++
                val document = Jsoup.parse(Utilities.get("$connectionUrl?page=$pageNumber&q=$searchQuery"))
                val repositories = document.body().getElementsByClass("ui repository list")[0]
                for (element in repositories.getElementsByClass("item")) {
                    val header = element.getElementsByClass("ui header")[0]
                    val name = element.getElementsByClass("name")[0]
                    val stats = header.getElementsByClass("ui right metas")[0].getElementsByTag("span")
                    val possibleDescription = element.getElementsByClass("has-emoji")
                    val holder = CodeRecyclerViewAdapter.DataHolder(
                        url = name.attr("href"),
                        icon = iconResource,
                        name = name.text(),
                        description = if (possibleDescription.size > 0) possibleDescription[0].text() else "",
                        modificationDate = element.getElementsByClass("time")[0].text(),
                        stars = stats[0].text().toInt(),
                        forks = stats[1].text().toInt()
                    )
                    activity?.runOnUiThread { data.add(holder) }
                }
                activity?.runOnUiThread {
                    if (data.size > 0) {
                        recyclerView.adapter?.notifyItemRangeInserted(pageNumber * 20 - 20, 20)
                        recyclerView.visibility = View.VISIBLE
                        nothingFoundWarning.visibility = View.GONE
                    }
                    else {
                        recyclerView.visibility = View.GONE
                        nothingFoundWarning.visibility = View.VISIBLE
                    }
                    if (pageNumber <= 1) {
                        // Scroll manually or else recycler scrolls to the bottom automatically
                        recyclerView.layoutManager?.scrollToPosition(0)
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
            catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

}
