package com.example.klsdinfo.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.Person2

class DanielServiceViewModelFactory constructor(private val repository: DanielServiceRepository,
                                                private val application: Application

                                           ) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass){
            when {
                isAssignableFrom(GroupViewModel::class.java) -> GroupViewModel(repository, application)



                else ->
                    throw IllegalArgumentException("Unknown Class")

            }
        } as T
    }

}

