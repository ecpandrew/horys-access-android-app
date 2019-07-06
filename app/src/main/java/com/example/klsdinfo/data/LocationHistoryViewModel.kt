package com.example.klsdinfo.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.*
import java.lang.Exception

class LocationHistoryViewModel(
    private val repo: DanielServiceRepository,
    application: Application

    ) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = false }


    private val listResource = MutableLiveData<List<TableFiveResource>>().apply { value = mutableListOf() }

    private val adapterData = MutableLiveData<MutableList<AuxResource5>>().apply { value = mutableListOf() }



    fun getAdapterData() : MutableLiveData<MutableList<AuxResource5>> {
        return adapterData
    }

    fun getResources() : MutableLiveData<List<TableFiveResource>>{
        return listResource
    }


    fun getProgress(): MutableLiveData<Boolean>{
        return loadingProgress
    }

    fun fetchData(){
        Log.i("retrofit", "fetch init")
        loadingProgress.postValue(true)

        repo.getPhysicalSpaceHistory({

            Log.i("retrofit", " fetch sucess")

            listResource.postValue(it)
            adapterData.postValue(generateData(it))
            loadingProgress.postValue(false)

        }, {
            Log.i("retrofit", "fetch failure")
            listResource.postValue(listOf())
            loadingProgress.postValue(false)


        })
    }



    private fun generateData(lista: List<TableFiveResource>): MutableList<AuxResource5> {
        val map2: MutableMap<String, MutableList<TableFiveResource>> = mutableMapOf()
        for (element in lista) {

            if (!map2.containsKey(element.physical_space)) {
                map2[element.physical_space] = mutableListOf(element)
            } else {
                val i: MutableList<TableFiveResource> = map2[element.physical_space]!!
                i.add(element)
                map2[element.physical_space] = i
            }
        }

        val lista: MutableList<AuxResource5> = mutableListOf()
        for (entry in map2){
            lista.add(AuxResource5(entry.key, entry.value))
        }
        return lista
    }




}