package com.smoothie.notabug

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.smoothie.notabug.view.EntityListItem

class PeopleRecyclerViewAdapter(fragment: PeopleRecyclerViewFragment, data: ArrayList<DataHolder>)
    : RecyclerViewWithFooterAdapter<PeopleRecyclerViewAdapter.DataHolder, PeopleRecyclerViewFragment, EntityListItem>(fragment, data) {

    data class DataHolder (
        val profilePicture: Drawable?,
        val username: String,
        val fullName:String,
        val email: String,
        val website: String,
        val location: String,
        val joinDate: String
    )

    override fun getListItemView(context: Context) = EntityListItem(context)

    override fun onBindItemViewHolder(view: EntityListItem, position: Int) {
        val data = getDataSet()[position]
        view.profilePicture = data.profilePicture
        view.username = data.username
        view.fullName = data.fullName
        view.email = data.email
        view.website = data.website
        view.location = data.location
        view.joinDate = data.joinDate
        if (position == 20 * getFragment().obtainPageNumber() - 10 - 1) getFragment().loadNewPage()
    }

}
