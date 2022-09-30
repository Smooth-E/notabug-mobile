package com.smoothie.notabug.anonymous

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smoothie.notabug.view.RepositoryListItem

class CodeRecyclerViewAdapter(private val dataSet : ArrayList<DataHolder>) : RecyclerView.Adapter<CodeRecyclerViewAdapter.ViewHolder>() {

    data class DataHolder (
        val url: String,
        val icon : Int,
        val name : String,
        val description: String,
        val modificationDate: String,
        val stars: Int,
        val forks: Int
        )

    class ViewHolder(view: RepositoryListItem) : RecyclerView.ViewHolder(view) {
        val view: RepositoryListItem

        init {
            this.view = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RepositoryListItem(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val dp = view.context.resources.displayMetrics.density
        val padding = (10 * dp).toInt()
        view.setPadding(padding, padding, padding, 0)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "Binding holder: $position")
        val data = dataSet[position]
        holder.view.icon = data.icon
        holder.view.name = data.name
        holder.view.name = data.name  // Doesn't work if set once for some reason
        holder.view.description = data.description
        holder.view.modificationDateString = data.modificationDate
        holder.view.starsAmount = data.stars
        holder.view.forksAmount = data.forks
    }

    override fun getItemCount(): Int = dataSet.size

}
