package com.example.klsdinfo.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.*
import java.lang.Exception

class PeopleHistoryViewModel(
    private val repo: DanielServiceRepository,
    application: Application

    ) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = false }


    private val listResource = MutableLiveData<List<TableFourResource>>().apply { value = mutableListOf() }

    private val adapterData = MutableLiveData<MutableList<AuxResource4>>().apply { value = mutableListOf() }



    fun getAdapterData() : MutableLiveData<MutableList<AuxResource4>> {
        return adapterData
    }

    fun getResources() : MutableLiveData<List<TableFourResource>>{
        return listResource
    }


    fun getProgress(): MutableLiveData<Boolean>{
        return loadingProgress
    }

    fun fetchData(){
        Log.i("retrofit", "fetch init")
        loadingProgress.postValue(true)

        repo.getPeopleHistory({

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



    private fun generateData(lista: List<TableFourResource>): MutableList<AuxResource4> {
        val map2: MutableMap<String, MutableList<TableFourResource>> = mutableMapOf()

        for (element in lista) {
            if (!map2.containsKey(element.shortName)) {
                map2[element.shortName] = mutableListOf(element)
            } else {
                val i: MutableList<TableFourResource> = map2[element.shortName]!!
                i.add(element)
                map2[element.shortName] = i
            }
        }

        val adapterData: MutableList<AuxResource4> = mutableListOf()
        for (entry in map2){
            adapterData.add(AuxResource4(entry.key, entry.value))
        }
        return adapterData
    }


    private fun getIds():String{
        var id = ""
//        for (element in mPeople.value!!.iterator()){
//            id+="${element.holder.id}/"
//        }
        return id
    }

}