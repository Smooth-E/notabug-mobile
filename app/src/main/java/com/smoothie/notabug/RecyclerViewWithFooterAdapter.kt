package com.smoothie.notabug

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class RecyclerViewWithFooterAdapter<DataHolderType, FragmentType, ListItemViewType: View>(
    private val fragment: FragmentType,
    private val dataSet: ArrayList<DataHolderType>,
    protected var continueLoading: Boolean = true
) : RecyclerView.Adapter<ViewHolder>() {

    protected inner class ViewHolderItem<ViewType: ListItemViewType>(view: ViewType) : ViewHolder(view) {
        val view: ViewType

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
            val footer = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_list_footer, parent, false)
            Log.d("continue", continueLoading.toString())
            if (!continueLoading) footer.visibility = View.GONE
            return ViewHolderFooter(footer)
        }
        val view = getListItemView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val dp = view.context.resources.displayMetrics.density
        val padding = (10 * dp).toInt()
        view.setPadding(padding, padding, padding, 0)
        return ViewHolderItem(view)
    }

    protected fun getDataSet() = dataSet

    protected fun getFragment() = fragment

    protected abstract fun onBindItemViewHolder(view: ListItemViewType, position: Int)

    fun stopLoadingNewPages() {
        continueLoading = false
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != dataSet.size) onBindItemViewHolder((holder as ViewHolderItem<ListItemViewType>).view, position)
    }
}