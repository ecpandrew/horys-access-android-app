package com.example.klsdinfo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [GroupQuery::class, LocalUserQuery::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupQueryDao
    abstract fun localUserDao(): LocalUserQueryDao


    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "appdatabase.db").fallbackToDestructiveMigration().build()

                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }

    }

}