package com.smoothie.notabug.anonymous

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smoothie.notabug.R
import com.smoothie.notabug.view.RepositoryListItem

class CodeRecyclerViewAdapter(private val fragment: CodeRecyclerViewFragment?, private val dataSet : ArrayList<DataHolder>) : RecyclerView.Adapter<ViewHolder>() {

    data class DataHolder (
        val url: String,
        val icon : Int,
        val name : String,
        val description: String,
        val modificationDate: String,
        val stars: Int,
        val forks: Int
        )

    class ViewHolderItem(view: RepositoryListItem) : ViewHolder(view) {
        val view: RepositoryListItem

        init {
            this.view = view
        }
    }

    class ViewHolderFooter(view: View) : ViewHolder(view)

    private companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_FOOTER = 1
    }

    override fun getItemViewType(position: Int): Int =
        if (position == dataSet.size) TYPE_FOOTER else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_FOOTER)
            return ViewHolderFooter(LayoutInflater.from(parent.context).inflate(R.layout.view_list_footer, parent, false))
        val view = RepositoryListItem(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val dp = view.context.resources.displayMetrics.density
        val padding = (10 * dp).toInt()
        view.setPadding(padding, padding, padding, 0)
        return ViewHolderItem(view)
    }

    override fun onBindViewHolder(unrecognizedHolder: ViewHolder, position: Int) {
        if (position != dataSet.size) {
            Log.d("TAG", "Binding holder: $position")
            val data = dataSet[position]
            val holder = unrecognizedHolder as ViewHolderItem
            holder.view.icon = data.icon
            holder.view.name = data.name
            holder.view.name = data.name  // Doesn't work if set once for some reason
            holder.view.description = data.description
            holder.view.modificationDateString = data.modificationDate
            holder.view.starsAmount = data.stars
            holder.view.forksAmount = data.forks
            if (position == 20 * fragment!!.getPageNumber() - 10 - 1) fragment.loadNewPage()
        }
    }

    override fun getItemCount(): Int = dataSet.size + 1

}
