package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace

class SelectLocationAndTimeViewModel(
    private val semanticRepository: SemanticRepository, application: Application) : ViewModel(), LifecycleObserver{


    val mPeople = MutableLiveData<List<PhysicalSpace>>().apply { value = emptyList() }
    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }


    fun fetchData(){
        loadingProgress.postValue(true)
        semanticRepository.getPhysicalSpaces({
            mPeople.postValue(it)
            loadingProgress.postValue(false)

            Log.e("debug", it.toString())

        }, {
            mPeople.postValue(listOf())
            loadingProgress.postValue(false)

        })
    }



}