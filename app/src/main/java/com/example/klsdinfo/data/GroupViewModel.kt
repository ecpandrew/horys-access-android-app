package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2

class GroupViewModel(
    private val semanticRepository: SemanticRepository,
    private val listOfPersons: List<Person2>,
    private val dateArr: List<Long>,
    application: Application

    ) : ViewModel(), LifecycleObserver{


    val mPeople = MutableLiveData<List<Person2>>().apply { value = emptyList() }
    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }



    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load(){

    }

    fun fetchData(){

    }



}