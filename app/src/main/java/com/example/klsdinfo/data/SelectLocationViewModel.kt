package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace

class SelectLocationViewModel(
    private val semanticRepository: SemanticRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val mPhysicalSpaces = MutableLiveData<List<PhysicalSpace>>().apply { value = emptyList() }



    fun fetchPhysicalSpaces(){
        loadingProgress.postValue(true)

        semanticRepository.getPhysicalSpaces({


            mPhysicalSpaces.postValue(it)
            loadingProgress.postValue(false)
            Log.e("debug", it.toString())
        }, {

//            fetchPhysicalSpaces()
            loadingProgress.postValue(false)

//            mPhysicalSpaces.postValue(listOf())
//            loadingProgress.postValue(false)
//            error.postValue(Pair(0,""))
        })
    }



}