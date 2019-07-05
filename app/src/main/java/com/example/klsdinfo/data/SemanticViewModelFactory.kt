package com.example.klsdinfo.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SemanticViewModelFactory constructor(private val repository: SemanticRepository,
                                           private val repository2: DanielServiceRepository?,
                                           private val application: Application


                                           ) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass){
            when {
                isAssignableFrom(ListPersonViewModel::class.java) -> ListPersonViewModel(repository, application)

                isAssignableFrom(ListPersonDateViewModel::class.java) -> ListPersonDateViewModel(repository, application)

                isAssignableFrom(ListLocationViewModel::class.java) -> ListLocationViewModel(repository, application)

                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository, repository2!!, application)

                else ->
                    throw IllegalArgumentException("Unknown Class")

            }
        } as T
    }




}

