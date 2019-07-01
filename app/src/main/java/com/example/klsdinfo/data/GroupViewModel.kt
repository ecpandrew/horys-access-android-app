package com.example.klsdinfo.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.AuxResource3
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableThreeResource
import java.lang.Exception

class GroupViewModel(
    private val repo: DanielServiceRepository,
    application: Application

    ) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = false }


    private val listResource = MutableLiveData<List<TableThreeResource>>().apply { value = mutableListOf() }

    private val adapterData = MutableLiveData<MutableList<AuxResource3>>().apply { value = mutableListOf() }




    fun getAdapterData() : MutableLiveData<MutableList<AuxResource3>> {
        return adapterData
    }

    fun getResources() : MutableLiveData<List<TableThreeResource>>{
        return listResource
    }


    fun getProgress(): MutableLiveData<Boolean>{
        return loadingProgress
    }

    fun fetchData(){
        Log.i("retrofit", "fetch init")
        loadingProgress.postValue(true)

        repo.getGroupRendezvous({

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



    private fun generateData(lista: List<TableThreeResource>
    ): MutableList<AuxResource3>{

        val aux: MutableList<AuxResource3> = mutableListOf()
        val map: MutableMap<String, MutableList<TableThreeResource>> = mutableMapOf()
        for (resource in lista){
            for(person in resource.persons){
                if(!map.containsKey(person.shortName)){
                    map[person.shortName] = mutableListOf(resource)
                }else{
                    val auxi = map[person.shortName]!!
                    auxi.add(resource)
                    map[person.shortName] = auxi
                }
            }
        }

        for (entry in map){
            aux.add(AuxResource3(entry.key, entry.value))
        }
        return aux
    }


    private fun getIds():String{
        var id = ""
//        for (element in mPeople.value!!.iterator()){
//            id+="${element.holder.id}/"
//        }
        return id
    }

}