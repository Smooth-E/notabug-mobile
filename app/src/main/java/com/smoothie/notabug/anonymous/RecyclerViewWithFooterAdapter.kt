package com.smoothie.notabug.anonymous

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smoothie.notabug.R

abstract class RecyclerViewWithFooterAdapter<DataHolderType, FragmentType, ListItemViewType: View>(
    private val fragment: FragmentType,
    private val dataSet: ArrayList<DataHolderType>
    ) : RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolderItem(view: ListItemViewType) : ViewHolder(view) {
        val view: ListItemViewType

        init {
            this.view = view
        }
    }

    inner class ViewHolderFooter(view: View) : ViewHolder(view)

    private companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_FOOTER = 1
    }

    override fun getItemCount(): Int = dataSet.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position == dataSet.size) TYPE_FOOTER else TYPE_ITEM

    abstract fun getListItemView(context: Context) : ListItemViewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_FOOTER) {
            return CodeRecyclerViewAdapter.ViewHolderFooter(LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_list_footer, parent, false))
        }
        val view = getListItemView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val dp = view.context.resources.displayMetrics.density
        val padding = (10 * dp).toInt()
        view.setPadding(padding, padding, padding, 0)
        return ViewHolderItem(view)
    }

}