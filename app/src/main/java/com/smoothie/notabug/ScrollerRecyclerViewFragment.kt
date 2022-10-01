package com.smoothie.notabug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class ScrollerRecyclerViewFragment<RecyclerViewAdapterType: RecyclerView.Adapter<*>, DataHolderType>
    : FadingFragment(R.layout.fragment_anonymous_scroller) {

    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var recyclerView: RecyclerView

    protected var data: ArrayList<DataHolderType> = ArrayList()

    protected var pageNumber = 0

    fun obtainPageNumber() = pageNumber

    protected abstract fun getAdapter(): RecyclerViewAdapterType

    abstract fun loadNewPage()

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

        val adapter = getAdapter()
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        loadNewPage()
    }
}
