package com.example.klsdinfo.repositories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SemanticViewModelFactory constructor(private val repository: SemanticRepository,
                                           private val application: Application


                                           ) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass){
            when {
                isAssignableFrom(ListPersonViewModel::class.java) ->
                    ListPersonViewModel(repository, application)

                else ->
                    throw IllegalArgumentException("Unknown Class")

            }
        } as T
    }
}

