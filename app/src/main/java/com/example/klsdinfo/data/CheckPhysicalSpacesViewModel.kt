package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableOneResource

class CheckPhysicalSpacesViewModel(
    private val danielServiceRepository: DanielServiceRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{


//    val resources = MutableLiveData<List<TableOneResource>>().apply { value = emptyList() }

    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }


    val adaterData = MutableLiveData<MutableMap<String, MutableList<TableOneResource>>>().apply { value = mutableMapOf() }


    fun fetchPeople(){
        loadingProgress.postValue(true)
        danielServiceRepository.getConnectedPeople(
            {
//                resources.postValue(it)
                adaterData.postValue(generateAdapterData(it))
                loadingProgress.postValue(false)
            },
            {
//                resources.postValue(listOf())
                loadingProgress.postValue(false)
            })

    }


    fun generateAdapterData(lista: List<TableOneResource>): MutableMap<String, MutableList<TableOneResource>> {
        val childMap: MutableMap<String, MutableList<TableOneResource>> = mutableMapOf()
        for (resource in lista) {
            if (!childMap.containsKey(resource.physical_space)) {
                childMap[resource.physical_space] = mutableListOf(resource)
            } else {
                val aux: MutableList<TableOneResource>? = childMap[resource.physical_space]
                aux?.add(resource)
                childMap[resource.physical_space] = aux!!
            }
        }
        return childMap
    }


}