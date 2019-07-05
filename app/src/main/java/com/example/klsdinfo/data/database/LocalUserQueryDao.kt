package com.example.klsdinfo.data.database

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*


@Dao
interface LocalUserQueryDao {
    @Query("SELECT * FROM localuserquery")
    fun getAll(): List<LocalUserQuery>

    @Query("DELETE  FROM localuserquery")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(query: LocalUserQuery)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(query: LocalUserQuery)

    @Transaction
    fun upsert(query : LocalUserQuery){
        try{
            insert(query)
        }catch (exception: SQLiteConstraintException){
            update(query)
        }
    }

}

