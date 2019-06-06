package com.example.KLSDinfo.UtilClasses

import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import android.widget.Checkable
import android.view.View
import android.widget.CheckedTextView
import com.example.KLSDinfo.R


class MultiCheckPersonViewHolder(itemView: View) : CheckableChildViewHolder(itemView){


    private val childCheckedTextView: CheckedTextView = itemView.findViewById(R.id.list_item_multicheck_artist_name)

    override fun getCheckable(): Checkable {
        return childCheckedTextView
    }

    fun setArtistName(artistName: String) {
        childCheckedTextView.text = artistName
    }
}