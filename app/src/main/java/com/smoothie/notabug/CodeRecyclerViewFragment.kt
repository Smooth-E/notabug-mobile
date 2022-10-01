package com.smoothie.notabug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.jsoup.Jsoup

open class CodeRecyclerViewFragment : FadingFragment(R.layout.fragment_anonymous_scroller) {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    protected var data: ArrayList<CodeRecyclerViewAdapter.DataHolder> = ArrayList()
    protected open val connectionUrl = "https://notabug.org"
    protected open val iconResource = R.drawable.favicon

    private var pageNumber = 0

    fun getPageNumber() = pageNumber

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        swipeRefreshLayout.setOnRefreshListener {
            pageNumber = 0
            val size = data.size
            for (index in 0 until size) data.removeFirst()
            recyclerView.adapter?.notifyItemRangeRemoved(0, size)
            loadNewPage()
        }

        val adapter = CodeRecyclerViewAdapter(this, data)
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        loadNewPage()
    }

    fun loadNewPage() = LoadingThread(this).start()

    inner class LoadingThread(private val fragment: CodeRecyclerViewFragment?) : Thread() {
        override fun run() {
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
    }

}
