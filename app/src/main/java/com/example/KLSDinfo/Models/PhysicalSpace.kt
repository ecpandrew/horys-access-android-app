package com.example.KLSDinfo.Models

import android.os.Parcel
import android.os.Parcelable


data class PhysicalSpace(val name:String, val description: String, val holder: Holder?, val children: List<PhysicalSpace>?) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<Holder>(Holder::class.java.classLoader),
        parcel.createTypedArrayList(CREATOR)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeTypedList(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhysicalSpace> {
        override fun createFromParcel(parcel: Parcel): PhysicalSpace {
            return PhysicalSpace(parcel)
        }

        override fun newArray(size: Int): Array<PhysicalSpace?> {
            return arrayOfNulls(size)
        }
    }

}



data class Holder(val id: Long): Parcelable{
    constructor(parcel: Parcel) : this(parcel.readLong())
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Holder> {
        override fun createFromParcel(parcel: Parcel): Holder {
            return Holder(parcel)
        }

        override fun newArray(size: Int): Array<Holder?> {
            return arrayOfNulls(size)
        }
    }


}