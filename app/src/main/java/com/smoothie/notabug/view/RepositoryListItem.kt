package com.smoothie.notabug.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.smoothie.notabug.R
import org.w3c.dom.Text

class RepositoryListItem : FrameLayout {

    private var icon = R.drawable.ic_baseline_class_24
    private var forksAmount = 5
    private var starsAmount = 16

    private lateinit var name : String
    private lateinit var description : String
    private lateinit var modificationDateString : String


    constructor(context: Context) : super(context) {
        buildView(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        buildView(context, attributeSet)
    }

    private fun buildView(context: Context, attributeSet: AttributeSet?) {
        val view = inflate(context, R.layout.view_repository_list_item, this)
        name = context.getString(R.string.placeholder_repository_name)
        description = context.getString(R.string.placeholder_repository_description)
        modificationDateString = context.getString(R.string.placeholder_last_modified)
        if (attributeSet != null) {
            val attributes = context.theme.obtainStyledAttributes(attributeSet, R.styleable.PillSearchBarView, 0, 0)
            icon = attributes.getResourceId(R.styleable.RepositoryListItem_icon, R.drawable.ic_baseline_class_24)
            name = attributes.getString(R.styleable.RepositoryListItem_name) ?: name
            description = attributes.getString(R.styleable.RepositoryListItem_description) ?: description
            modificationDateString = attributes.getString(R.styleable.RepositoryListItem_modificationTimeString) ?: modificationDateString
            forksAmount = attributes.getInteger(R.styleable.RepositoryListItem_forksAmount, 16)
            starsAmount = attributes.getInteger(R.styleable.RepositoryListItem_starsAmount, 5)
        }
        view.findViewById<TextView>(R.id.name).text = name
        view.findViewById<ImageView>(R.id.icon).setImageResource(icon)
        view.findViewById<TextView>(R.id.description).text = description
        view.findViewById<TextView>(R.id.date_modified).text = modificationDateString
        view.findViewById<TextView>(R.id.stars).text = starsAmount.toString()
        view.findViewById<TextView>(R.id.forks).text = forksAmount.toString()
    }
}