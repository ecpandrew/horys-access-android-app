package com.example.klsdinfo.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groupquery")
data class GroupQuery (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "ids") val ids: String?,
    @ColumnInfo(name = "past_date") val pastDate: String?,
    @ColumnInfo(name = "current_date") val currentDate: String?
    )