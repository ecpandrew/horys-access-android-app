package com.example.klsdinfo.data.models

import android.os.Parcel
import android.os.Parcelable


data class Location(val name: String, val description: String, var image: Int?, val childs:List<Location>?, val color: Int = -1) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(CREATOR)!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        image?.let { parcel.writeInt(it)
        parcel.writeTypedList(childs)
        parcel.writeInt(color)}
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}