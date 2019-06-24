package com.example.klsdinfo.data.database

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*


@Dao
interface GroupQueryDao {

    @Query("SELECT * FROM groupquery")
    fun getAll(): List<GroupQuery>

    @Query("DELETE  FROM groupquery")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(query: GroupQuery)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(query: GroupQuery)

    @Transaction
    fun upsert(query : GroupQuery){
        try{
            insert(query)
        }catch (exception: SQLiteConstraintException){
            update(query)
        }
    }

}