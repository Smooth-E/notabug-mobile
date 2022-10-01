package com.smoothie.notabug.anonymous

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smoothie.notabug.R
import com.smoothie.notabug.view.RepositoryListItem

class CodeRecyclerViewAdapter(data: ArrayList<DataHolder>, fragment: CodeRecyclerViewFragment)
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

    override fun onBindItemViewHolder(holder: ViewHolderItem<RepositoryListItem>, position: Int) {
        Log.d("TAG", "Binding holder: $position")
        val data = getDataSet()[position]
        holder.view.icon = data.icon
        holder.view.name = data.name
        holder.view.name = data.name  // Doesn't work if set once for some reason
        holder.view.description = data.description
        holder.view.modificationDateString = data.modificationDate
        holder.view.starsAmount = data.stars
        holder.view.forksAmount = data.forks
        if (position == 20 * getFragment().getPageNumber() - 10 - 1) getFragment().loadNewPage()
    }

}
