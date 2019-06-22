package com.example.klsdinfo.models

import android.os.Parcel
import android.os.Parcelable


data class Method(val name: String, val description: String, var image: Int?) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        image?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Method> {
        override fun createFromParcel(parcel: Parcel): Method {
            return Method(parcel)
        }

        override fun newArray(size: Int): Array<Method?> {
            return arrayOfNulls(size)
        }
    }
}