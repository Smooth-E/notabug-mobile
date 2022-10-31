package com.smoothie.notabug

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class ScrollerRecyclerViewFragment<
        RecyclerViewAdapterType: RecyclerView.Adapter<*>,
        DataHolderType: java.io.Serializable
        >(private val itemsOnPage: Int)
    : FadingFragment(R.layout.fragment_refreshable_recycler) {

    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var recyclerView: RecyclerView
    protected var thread: Thread? = null

    protected var data: ArrayList<DataHolderType> = ArrayList()

    protected var pageNumber = 0

    fun obtainPageNumber() = pageNumber

    protected abstract fun getAdapter(): RecyclerViewAdapterType

    fun loadNewPage() = loadNewPage(false)

    abstract fun loadNewPage(isReloading: Boolean)

    private fun reloadList() {
        thread?.interrupt()
        pageNumber = 0
        loadNewPage(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        val typedValueSurface = TypedValue()
        val typedValueOnSurface = TypedValue()
        val typedValueSecondaryContainer = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValueSurface, true)
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValueOnSurface, true)
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValueSecondaryContainer, true)
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(typedValueSurface.data)
        swipeRefreshLayout.setColorSchemeColors(typedValueOnSurface.data, typedValueSecondaryContainer.data)
        swipeRefreshLayout.setOnRefreshListener { reloadList() }

        val adapter = getAdapter()
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        if (pageNumber == 0) loadNewPage()
    }

}
