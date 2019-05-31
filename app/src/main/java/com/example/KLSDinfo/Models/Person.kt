package com.example.KLSDinfo.Models

import android.os.Parcel
import android.os.Parcelable

data class Person(val name:String, val isFavorite: Boolean) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Person) return false

        val artist = o as Person?

        if (isFavorite != artist!!.isFavorite) return false
        return name == artist.name

    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (isFavorite) 1 else 0
        return result
    }

    //    companion object {
//
//        val CREATOR: Parcelable.Creator<Person> = object : Parcelable.Creator<Person> {
//            override fun createFromParcel(`in`: Parcel): Person {
//                return Person(`in`)
//            }
//
//            override fun newArray(size: Int): Array<Person?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}


