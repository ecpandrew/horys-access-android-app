package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableOneResource
import com.example.klsdinfo.data.models.TableTwoResource

class FindPeopleViewModel(
    private val danielServiceRepository: DanielServiceRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{


//    val resources = MutableLiveData<List<TableOneResource>>().apply { value = emptyList() }

    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }


    val adaterData = MutableLiveData<MutableMap<String, MutableList<TableTwoResource>>>().apply { value = mutableMapOf() }



    fun fetchData(){
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



    fun generateAdapterData(lista : List<TableTwoResource>): MutableMap<String, MutableList<TableTwoResource>>{

        Log.i("adapter", lista.toString())
        val map : MutableMap<String, MutableList<TableTwoResource>> = mutableMapOf()
        map["No Role"] = mutableListOf()
        for (element in lista){

            if(element.roles.isNullOrEmpty()){
                val aux = map["No Role"] as MutableList
                aux.add(element)
                map["No Role"] = aux
            }
            else{

                for (role in element.roles){

                    val aux = map[role.name] ?: mutableListOf()

                    aux.add(element)

                    map[role.name] = aux



                }
            }
        }
        if (map["No Role"]!!.isEmpty()){map.remove("No Role")}
        return map
    }



}