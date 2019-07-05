package com.example.klsdinfo.data.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localuserquery")
data class LocalUserQuery (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "email") val email: String
//    @ColumnInfo(name = "holder") val holder: Long,
//    @ColumnInfo(name = "shortName") val shortName: String,
//    @ColumnInfo(name = "roles" ) val roles: String?,
//    @ColumnInfo(name = "fullName") val fullName: String?
)
