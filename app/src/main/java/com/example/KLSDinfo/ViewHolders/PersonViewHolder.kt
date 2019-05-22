package com.example.KLSDinfo.ViewHolders

import android.view.View
import android.widget.TextView
import com.example.KLSDinfo.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PersonViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val childTextView: TextView = itemView.findViewById(R.id.list_item_artist_name)

    fun setArtistName(name: String) {
        childTextView.text = name
    }


}