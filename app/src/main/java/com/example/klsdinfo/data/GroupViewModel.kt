package com.example.klsdinfo.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableThreeResource
import java.lang.Exception

class GroupViewModel(
    private val repo: DanielServiceRepository,
    application: Application

    ) : AndroidViewModel(application), LifecycleObserver{


//    val date1 = MutableLiveData<String>().apply { value = dateArr[0] }
//
//    val date2 = MutableLiveData<String>().apply { value = dateArr[1] }
//
//    val mPeople = MutableLiveData<List<Person2>>().apply { value = listOfPersons }

    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    private val listResource = MutableLiveData<List<TableThreeResource>>().apply { value = mutableListOf() }

    val listResource2 = MutableLiveData<String>().apply { value = "" }


    fun getResources() : MutableLiveData<List<TableThreeResource>>{
        return listResource
    }

    fun fetchData(){
        Log.i("retrofit", "fetch init")

        this.repo.getGroupRendezvous({
            Log.i("retrofit", " fetch sucess")
            listResource.postValue(it)
        }, {
            Log.i("retrofit", "fetch failure")

        })
    }


    private fun getIds():String{
        var id = ""
//        for (element in mPeople.value!!.iterator()){
//            id+="${element.holder.id}/"
//        }
        return id
    }

}