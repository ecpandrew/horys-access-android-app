package com.example.KLSDinfo.Models

import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

data class Role(val name: String, val persons: List<Person>, val iconResId: Int) : ExpandableGroup<Person>(name, persons) {



    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Role) return false

        val role = o as Role?

        return iconResId == role!!.iconResId

    }

    override fun hashCode(): Int {
        return iconResId
    }
}