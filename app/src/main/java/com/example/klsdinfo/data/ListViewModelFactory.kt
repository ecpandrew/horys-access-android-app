package com.example.klsdinfo.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModelFactory constructor(private val repository: SemanticRepository,
                                       private val application: Application


                                           ) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass){
            when {
                isAssignableFrom(ListPersonViewModel::class.java) ->
                    ListPersonViewModel(repository, application)

                isAssignableFrom(ListPersonDateViewModel::class.java) ->
                    ListPersonDateViewModel(repository, application)


                else ->
                    throw IllegalArgumentException("Unknown Class")

            }
        } as T
    }




}

