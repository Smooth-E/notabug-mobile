package com.smoothie.notabug

import android.content.Context
import android.util.Log
import com.smoothie.notabug.view.RepositoryListItem

class CodeRecyclerViewAdapter(fragment: CodeRecyclerViewFragment, data: ArrayList<DataHolder>)
    : RecyclerViewWithFooterAdapter<CodeRecyclerViewAdapter.DataHolder, CodeRecyclerViewFragment, RepositoryListItem>(fragment, data) {

    data class DataHolder (
        val url: String,
        val icon : Int,
        val name : String,
        val description: String,
        val modificationDate: String,
        val stars: Int,
        val forks: Int
        )

    override fun getListItemView(context: Context): RepositoryListItem = RepositoryListItem(context)

    override fun onBindItemViewHolder(view: RepositoryListItem, position: Int) {
        val data = getDataSet()[position]
        view.icon = data.icon
        view.name = data.name
        view.name = data.name  // Doesn't work if set once for some reason
        view.description = data.description
        view.modificationDateString = data.modificationDate
        view.starsAmount = data.stars
        view.forksAmount = data.forks
        if (position == 20 * getFragment().obtainPageNumber() - 10 - 1) getFragment().loadNewPage()
    }

}
