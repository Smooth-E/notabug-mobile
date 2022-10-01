package com.smoothie.notabug.anonymous

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smoothie.notabug.R
import com.smoothie.notabug.view.EntityListItem

class PeopleRecyclerViewAdapter(private val fragment: PeopleRecyclerViewFragment?, private var dataSet: ArrayList<DataHolder>) //: RecyclerView.Adapter<ViewHolder>() {
{
    data class DataHolder (
        val profilePicture: Drawable,
        val username: String,
        val fullName:String,
        val email: String,
        val website: String,
        val location: String,
        val joinDate: String
    )



}