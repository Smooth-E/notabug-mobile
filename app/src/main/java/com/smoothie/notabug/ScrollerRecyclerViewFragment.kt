package com.smoothie.notabug

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.enableSavedStateHandles
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class ScrollerRecyclerViewFragment<
        RecyclerViewAdapterType: RecyclerView.Adapter<*>,
        DataHolderType: java.io.Serializable
        >(private val itemsOnPage: Int)
    : FadingFragment(R.layout.fragment_refreshable_recycler) {

    companion object {
        private val ITEM_LAST_LOADED_PAGE = "LAST_LOADED_PAGE"
        private val ITEM_LIST_DATA = "LIST_DATA"
    }

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

    /*
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            pageNumber = savedInstanceState.getInt(ITEM_LAST_LOADED_PAGE)
            val savedData = savedInstanceState.getSerializable(ITEM_LIST_DATA) as ArrayList<DataHolderType>
            for (element in savedData) data.add(element)
        }
        else Log.w("State is null", "State is null")
    }
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        swipeRefreshLayout.setOnRefreshListener { reloadList() }

        val adapter = getAdapter()
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        if (pageNumber == 0) loadNewPage()
    }

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val dataToSave : ArrayList<DataHolderType> = ArrayList()
        for (index in 0 until pageNumber * itemsOnPage - itemsOnPage)
            dataToSave.add(data[index])
        outState.putSerializable(ITEM_LIST_DATA, dataToSave)
        outState.putInt(ITEM_LAST_LOADED_PAGE, pageNumber - 1)
    }
     */

}
