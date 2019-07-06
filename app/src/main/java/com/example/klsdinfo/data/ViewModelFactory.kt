package com.example.klsdinfo.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory constructor(private val semanticRepository: SemanticRepository?,
                                   private val danielRepository: DanielServiceRepository?,
                                   private val application: Application


                                           ) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass){
            when {
                isAssignableFrom(PeopleHistoryViewModel::class.java) -> PeopleHistoryViewModel(danielRepository!!, application)

                isAssignableFrom(GroupHistoryViewModel::class.java) -> GroupHistoryViewModel(danielRepository!!, application)

                isAssignableFrom(SelectPersonViewModel::class.java) -> SelectPersonViewModel(semanticRepository!!, application)

                isAssignableFrom(SelectPersonAndTimeViewModel::class.java) -> SelectPersonAndTimeViewModel(semanticRepository!!, application)

                isAssignableFrom(SelectLocationViewModel::class.java) -> SelectLocationViewModel(semanticRepository!!, application)

                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(semanticRepository!!, danielRepository!!, application)

                else ->
                    throw IllegalArgumentException("Unknown Class")

            }
        } as T
    }




}

