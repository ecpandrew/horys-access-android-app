package com.example.KLSDinfo.Models

import android.os.Parcel
import android.os.Parcelable


data class PhysicalSpace(val name:String, val description: String?, val holder: Holder, val children: List<PhysicalSpace>?) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readParcelable<Holder>(Holder::class.java.classLoader)!!,
        parcel.createTypedArrayList(CREATOR)!!
    )

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

data class Role2(val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Role2> {
        override fun createFromParcel(parcel: Parcel): Role2 {
            return Role2(parcel)
        }

        override fun newArray(size: Int): Array<Role2?> {
            return arrayOfNulls(size)
        }
    }

}


data class Person2(val email:String, val shortName:String, val fullName:String?, val roles: List<Role2>?, val holder: Holder): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createTypedArrayList(Role2.CREATOR),
        parcel.readParcelable(Holder::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(shortName)
        parcel.writeString(fullName)
        parcel.writeTypedList(roles)
        parcel.writeParcelable(holder, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person2> {
        override fun createFromParcel(parcel: Parcel): Person2 {
            return Person2(parcel)
        }

        override fun newArray(size: Int): Array<Person2?> {
            return arrayOfNulls(size)
        }
    }
}


data class TableTwoResource(val shortName: String, val physical_space: String, val thingID: String, val duration: Long): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeString(physical_space)
        parcel.writeString(thingID)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TableTwoResource> {
        override fun createFromParcel(parcel: Parcel): TableTwoResource {
            return TableTwoResource(parcel)
        }

        override fun newArray(size: Int): Array<TableTwoResource?> {
            return arrayOfNulls(size)
        }
    }

}



data class TableFourResource(val shortName: String, val physical_space: String, val thingID: String, val arrive: Long, val depart: Long){
    fun getDuration(): Long{
        return (this.depart - this.arrive)
    }
}

data class TableThreeResource(val physical_space: String, val persons: ArrayList<ShortPerson>, val arrive: Long, val depart: Long) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(ShortPerson)!!,
        parcel.readLong(),
        parcel.readLong()
    )
    fun getDuration(): Long{
        return (this.depart - this.arrive)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(physical_space)
        parcel.writeTypedList(persons)
        parcel.writeLong(arrive)
        parcel.writeLong(depart)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TableThreeResource> {
        override fun createFromParcel(parcel: Parcel): TableThreeResource {
            return TableThreeResource(parcel)
        }

        override fun newArray(size: Int): Array<TableThreeResource?> {
            return arrayOfNulls(size)
        }
    }

}

data class ShortPerson(val shortName: String): Parcelable{
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<ShortPerson> {
        override fun createFromParcel(parcel: Parcel): ShortPerson {
            return ShortPerson(parcel)
        }

        override fun newArray(size: Int): Array<ShortPerson?> {
            return arrayOfNulls(size)
        }
    }

}

