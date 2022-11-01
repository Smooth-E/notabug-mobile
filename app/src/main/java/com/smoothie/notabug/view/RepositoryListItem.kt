package com.smoothie.notabug.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R
import org.w3c.dom.Text

class RepositoryListItem : FrameLayout {

    private val centeredDialogStyle =
        com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog

    private lateinit var viewClickable: View
    private lateinit var imageViewIcon : ImageView
    private lateinit var textViewName : TextView
    private lateinit var textViewDescription : TextView
    private lateinit var textViewModificationDate : TextView
    private lateinit var textViewStars : TextView
    private lateinit var textViewForks : TextView

    var icon = R.drawable.ic_baseline_class_24
        set (value) {
            imageViewIcon.setImageResource(value)
            field = value
        }

    var name : String = context.getString(R.string.placeholder_repository_name)
        set (value) {
            textViewName.text = name
            field = value
        }

    var description : String? = context.getString(R.string.placeholder_repository_description)
        set (value) {
            if (value != null && value.isNotEmpty()) {
                textViewDescription.text = value
                textViewDescription.visibility = View.VISIBLE
            }
            else textViewDescription.visibility = View.GONE
            field = value
        }

    var modificationDateString : String = context.getString(R.string.placeholder_last_modified)
        set (value) {
            textViewModificationDate.text = value
            field = value
        }

    var forksAmount = 5
        set (value) {
            textViewForks.text = value.toString()
            field = value
        }

    var starsAmount = 16
        set (value) {
            textViewStars.text = value.toString()
            field = value
        }

    constructor(context: Context) : super(context) {
        buildView(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        buildView(context, attributeSet)
    }

    private fun buildView(context: Context, attributeSet: AttributeSet?) {
        val view = inflate(context, R.layout.view_repository_list_item, this)

        viewClickable = view.findViewById(R.id.view_clickable)
        imageViewIcon = view.findViewById(R.id.icon)
        textViewName = view.findViewById(R.id.name)
        textViewDescription = view.findViewById(R.id.description)
        textViewModificationDate = view.findViewById(R.id.date_modified)
        textViewStars = view.findViewById(R.id.stars)
        textViewForks = view.findViewById(R.id.forks)

        if (attributeSet != null) {
            val attributes = context.theme.obtainStyledAttributes(attributeSet, R.styleable.PillSearchBarView, 0, 0)
            icon = attributes.getResourceId(R.styleable.RepositoryListItem_icon, R.drawable.ic_baseline_class_24)
            name = attributes.getString(R.styleable.RepositoryListItem_name) ?: name
            description = attributes.getString(R.styleable.RepositoryListItem_description) ?: description
            modificationDateString = attributes.getString(R.styleable.RepositoryListItem_modificationTimeString) ?: modificationDateString
            forksAmount = attributes.getInteger(R.styleable.RepositoryListItem_forksAmount, 16)
            starsAmount = attributes.getInteger(R.styleable.RepositoryListItem_starsAmount, 5)
        }

        viewClickable.setOnLongClickListener {
            val builder = MaterialAlertDialogBuilder(it.context, centeredDialogStyle)
            builder.setIcon(icon)
            builder.setTitle(name)
            if (description!!.trim().isNotEmpty()) builder.setMessage(description)
            builder.setNeutralButton(R.string.action_cancel) { dialog, _ -> dialog.dismiss() }
            builder.setPositiveButton(R.string.action_repository) { dialog, _ -> Toast.makeText(this.context, "Author", Toast.LENGTH_SHORT).show() }
            builder.setNegativeButton(R.string.action_author) { dialog, _ -> Toast.makeText(this.context, "Author", Toast.LENGTH_SHORT).show() }
            builder.show()
            true
        }
    }

}
