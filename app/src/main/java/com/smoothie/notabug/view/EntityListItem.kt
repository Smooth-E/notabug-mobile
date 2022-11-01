package com.smoothie.notabug.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.smoothie.notabug.R

class EntityListItem : FrameLayout {

    private lateinit var imageViewProfilePicture : ImageView
    private lateinit var textViewUsername : TextView
    private lateinit var parentViewGroupFullName : ViewGroup
    private lateinit var textViewFullName : TextView
    private lateinit var parentViewGroupEmail : ViewGroup
    private lateinit var textViewEmail : TextView
    private lateinit var parentViewGroupWebsite : ViewGroup
    private lateinit var textViewWebsite : TextView
    private lateinit var parentViewGroupLocation : ViewGroup
    private lateinit var textViewLocation : TextView
    private lateinit var parentViewGroupJoinDate : ViewGroup
    private lateinit var textViewJoinDate : TextView

    var profilePicture : Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.favicon, context.theme)
        set(value) {
            imageViewProfilePicture.setImageDrawable(value)
            field = value
        }

    var username : String? = context.getString(R.string.placeholder_username)
        set(value) {
            if (value!!.isEmpty()) throw  IllegalArgumentException("Username cannot be empty!")
            textViewUsername.text = value
            field = value
        }

    var fullName : String? = context.getString(R.string.placeholder_full_name)
        set(value) {
            applyText(value, textViewFullName, parentViewGroupFullName)
            field = value
        }

    var email : String? = context.getString(R.string.placeholder_email)
        set(value) {
            applyText(value, textViewEmail, parentViewGroupEmail)
            field = value
        }

    var website : String? = context.getString(R.string.placeholder_website)
        set(value) {
            applyText(value, textViewWebsite, parentViewGroupWebsite)
            field = value
        }

    var location : String?  = context.getString(R.string.placeholder_location)
        set(value) {
            applyText(value, textViewLocation, parentViewGroupLocation)
            field = value
        }

    var joinDate : String? = context.getString(R.string.placeholder_join_date)
        set(value) {
            applyText(value, textViewJoinDate, parentViewGroupJoinDate)
            field = value
        }

    constructor(context: Context) : super(context) {
        buildView(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        buildView(context, attributeSet)
    }

    private fun buildView(context: Context, attributeSet: AttributeSet?) {
        val view = inflate(context, R.layout.view_entity_list_entry, this)

        imageViewProfilePicture = view.findViewById(R.id.profile_picture)
        textViewUsername = view.findViewById(R.id.username)
        parentViewGroupFullName = view.findViewById(R.id.parent_full_name)
        textViewFullName = view.findViewById(R.id.full_name)
        parentViewGroupEmail = view.findViewById(R.id.parent_email)
        textViewEmail = view.findViewById(R.id.email)
        parentViewGroupWebsite = view.findViewById(R.id.parent_website)
        textViewWebsite = view.findViewById(R.id.website)
        parentViewGroupLocation = view.findViewById(R.id.parent_location)
        textViewLocation = view.findViewById(R.id.location)
        parentViewGroupJoinDate = view.findViewById(R.id.parent_join_date)
        textViewJoinDate = view.findViewById(R.id.join_date)

        if (attributeSet != null) {
            val attributes = context.theme.obtainStyledAttributes(attributeSet, R.styleable.EntityListItem, 0, 0)
            profilePicture = ResourcesCompat.getDrawable(resources, attributes.getResourceId(R.styleable.EntityListItem_icon, R.drawable.favicon), context.theme)
            username = attributes.getString(R.styleable.EntityListItem_username)
            fullName = attributes.getString(R.styleable.EntityListItem_fullName)
            email = attributes.getString(R.styleable.EntityListItem_email)
            website = attributes.getString(R.styleable.EntityListItem_website)
            location = attributes.getString(R.styleable.EntityListItem_location)
            joinDate = attributes.getString(R.styleable.EntityListItem_joinDateString)
        }
    }

    private fun applyText(text: String?, textView: TextView, parent: ViewGroup) {
        parent.visibility = if (text == null || text.isEmpty()) View.GONE else View.VISIBLE
        textView.text = text
    }

}
