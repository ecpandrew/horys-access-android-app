package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2

class SelectPersonAndTimeViewModel(
    private val semanticRepository: SemanticRepository, application: Application) : ViewModel(), LifecycleObserver{


    val mPeople = MutableLiveData<List<Person2>>().apply { value = emptyList() }
    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load(){
        semanticRepository.getAvailablePeople({
            mPeople.postValue(it)
            Log.e("debug", it.toString())

        }, {
            mPeople.postValue(listOf())
        })

    }

    fun fetchPeople(){
        loadingProgress.postValue(true)
        semanticRepository.getAvailablePeople({
            mPeople.postValue(it)
            loadingProgress.postValue(false)

            Log.e("debug", it.toString())

        }, {
            mPeople.postValue(listOf())
            loadingProgress.postValue(false)

        })
    }



}