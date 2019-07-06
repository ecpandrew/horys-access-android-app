package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace
import com.example.klsdinfo.data.models.TableFourResource
import com.example.klsdinfo.data.models.TableThreeResource

class HomeViewModel(

    private val semanticRepository: SemanticRepository,  private val danielServiceRepository: DanielServiceRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val user = MutableLiveData<Person2?>().apply { value = null }

    var email = MutableLiveData<String>().apply{ value = ""}


    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }

    val listResource = MutableLiveData<List<TableFourResource>>().apply { value = mutableListOf() }



    fun setDates(unixTime:String, unixTimePast: String){
        date1.value = unixTime
        date2.value = unixTimePast
    }




    fun fetchUser(){
        Log.e("debug", "fechUser()")

        loadingProgress.postValue(true)
        semanticRepository.getUserFromSemanticAndStore({
            loadingProgress.postValue(false)
            user.postValue(it)
            Log.e("debug", "dates ${date2.value}, ${date1.value}")

            fetchData(it.holder.id.toString(), date1.value!!, date2.value!! )
            Log.e("debug", "success() $it")
        }, {
            loadingProgress.postValue(false)
            user.postValue(null)
            Log.e("debug", "failure()")

        })
    }


    fun fetchData(id: String, date1:String, date2:String){

        danielServiceRepository.getPhysicalSpacesByPersonAndTime(id, date1,date2,
            {
                listResource.postValue(it)
            },
            {
                user.postValue(null)

            })





    }


}