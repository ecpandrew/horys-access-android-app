package com.example.klsdinfo.data.models

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
        parcel.createTypedArrayList(Role2),
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


data class Table4Aux(val name: String, val count_places: Int, val duration: Long): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(count_places)
        dest.writeLong(duration)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Table4Aux> {
        override fun createFromParcel(parcel: Parcel): Table4Aux {
            return Table4Aux(parcel)
        }

        override fun newArray(size: Int): Array<Table4Aux?> {
            return arrayOfNulls(size)
        }
    }
}

data class TableTwoResource(val shortName: String, val roles: List<Role2>?, val physical_space: String, val thingID: String, val duration: Long): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(Role2),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeTypedList(roles)
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

data class AuxResource4 (val name: String, val resources: MutableList<TableFourResource>){

    fun getplacesCount(): Int{
        val map : MutableMap<String, Unit> = mutableMapOf()

        for(i in resources){
            if(!map.containsKey(i.physical_space)) map[i.physical_space] = Unit

        }
        return map.size
    }

    fun getDuration(): Long{

        var dur: Long = 0
        for(i in resources){
            dur += i.getDuration()
        }
        return dur
    }



}


data class AuxResource5 (val name: String, val resources: MutableList<TableFiveResource>){

    fun getPersonCount(): Int{
        val map : MutableMap<String, Unit> = mutableMapOf()

        for(i in resources){
            if(!map.containsKey(i.shortName)) map[i.shortName] = Unit

        }
        return map.size
    }

    fun getDuration(): Long{

        var dur: Long = 0
        for(i in resources){
            dur += i.getDuration()
        }
        return dur
    }

}

data class AuxResource1 (val place: String, val resources: MutableList<TableOneResource>){

    fun getPersonCount(): Int{
        val map : MutableMap<String, Unit> = mutableMapOf()
        for(i in resources){
            if(!map.containsKey(i.shortName)) map[i.shortName] = Unit

        }
        return map.size
    }


}



data class AuxResource3 (val nome: String, val resources: MutableList<TableThreeResource>)


data class TableFiveResource(val shortName: String, val email: String, val physical_space: String, val arrive: Long, val depart: Long): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    fun getDuration(): Long{
        return (this.depart - this.arrive)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeString(email)
        parcel.writeString(physical_space)
        parcel.writeLong(arrive)
        parcel.writeLong(depart)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TableFourResource> {
        override fun createFromParcel(parcel: Parcel): TableFourResource {
            return TableFourResource(parcel)
        }

        override fun newArray(size: Int): Array<TableFourResource?> {
            return arrayOfNulls(size)
        }
    }
}


data class TableFourResource(val shortName: String, val physical_space: String, val thingID: String, val arrive: Long, val depart: Long): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    fun getDuration(): Long{
        return (this.depart - this.arrive)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeString(physical_space)
        parcel.writeString(thingID)
        parcel.writeLong(arrive)
        parcel.writeLong(depart)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TableFourResource> {
        override fun createFromParcel(parcel: Parcel): TableFourResource {
            return TableFourResource(parcel)
        }

        override fun newArray(size: Int): Array<TableFourResource?> {
            return arrayOfNulls(size)
        }
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
    fun getPersons(): String{
        var p = ""
        for (person in persons){
            p += "${person.shortName}, "
        }
        return p.substring(0, p.length-2)
    }
    fun getPersonsList(): List<String>{
        val x: MutableList<String> = mutableListOf()
        for (person in persons){
            x.add(person.shortName)
        }
        return x
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



data class TableOneResource(val shortName: String, val email: String, val physical_space: String, val duration: Long): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    ) {
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeString(email)
        parcel.writeString(physical_space)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TableFourResource> {
        override fun createFromParcel(parcel: Parcel): TableFourResource {
            return TableFourResource(parcel)
        }

        override fun newArray(size: Int): Array<TableFourResource?> {
            return arrayOfNulls(size)
        }
    }
}




data class ShortPerson(val shortName: String) : Parcelable{
//
    constructor(parcel: Parcel) : this(
        parcel.readString()!!
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
    }

    override fun describeContents(): Int {
        return 0
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

