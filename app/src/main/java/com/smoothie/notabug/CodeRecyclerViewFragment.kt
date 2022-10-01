package com.smoothie.notabug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.jsoup.Jsoup

open class CodeRecyclerViewFragment : ScrollerRecyclerViewFragment<CodeRecyclerViewAdapter, CodeRecyclerViewAdapter.DataHolder>() {

    private lateinit var thread: LoadingThread
    protected open val connectionUrl = "https://notabug.org"
    protected open val iconResource = R.drawable.ic_baseline_class_24

    override fun getAdapter(): CodeRecyclerViewAdapter = CodeRecyclerViewAdapter(this, data)

    override fun loadNewPage() = LoadingThread().start()

    override fun onDestroyView() {
        super.onDestroyView()
        thread.interrupt()
    }

    inner class LoadingThread() : Thread() {
        init {
            thread = this
        }

        override fun run() {
            try {
                pageNumber++
                val document = Jsoup.connect("${connectionUrl}?page=$pageNumber").get()
                val repositories = document.body().getElementsByClass("ui repository list")[0]
                activity?.runOnUiThread {
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
                        data.add(holder)
                        if (pageNumber > 1) recyclerView.adapter?.notifyItemInserted(data.size - 1)
                    }
                    if (pageNumber <= 1) {
                        recyclerView.layoutManager?.scrollToPosition(0)
                        (recyclerView.adapter as CodeRecyclerViewAdapter).notifyItemRangeInserted(0, 20)
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
