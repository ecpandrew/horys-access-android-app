package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableFourResource
import com.example.klsdinfo.data.models.TableTwoResource


class HomeViewModel(

    private val semanticRepository: SemanticRepository,  private val danielServiceRepository: DanielServiceRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{



    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    val user = MutableLiveData<Person2?>().apply { value = null }

    var email = MutableLiveData<String>().apply{ value = ""}


    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }

    val listResource = MutableLiveData<List<TableFourResource>>().apply { value = mutableListOf() }

    val chartData = MutableLiveData<Map<String,Long>>().apply { value = mapOf() }

    val currentPosition = MutableLiveData<List<TableTwoResource>>().apply { value = mutableListOf() }


    fun setDates(unixTime:String, unixTimePast: String){
        date1.value = unixTime
        date2.value = unixTimePast
    }



    fun fetchCurrentPosition(id: String){


        danielServiceRepository.getCurrentPosition(id,
            {
                currentPosition.postValue(it)
            },
            {
                currentPosition.postValue(null)
            })


    }

    fun fetchUserForCurrentPosition(){
        Log.e("debug", "fechUser()")

        loadingProgress.postValue(true)
        semanticRepository.getUserFromSemanticAndStore({

            fetchCurrentPosition(it.holder.id.toString())
        }, {
            //            loadingProgress.postValue(false)
//            user.postValue(null)
            Log.e("debug", "failure()")

        })
    }
//            val radio : RadioButton = mView.findViewById(id)





    fun fetchUserForChart(){
        Log.e("debug", "fechUser()")

        loadingProgress.postValue(true)
        semanticRepository.getUserFromSemanticAndStore({
            loadingProgress.postValue(false)
            user.postValue(it)
            Log.e("debug", "dates ${date2.value}, ${date1.value}")

            fetchData(it.holder.id.toString(), date1.value!!, date2.value!! )
            Log.e("debug", "success() $it")
        }, {
//            loadingProgress.postValue(false)
//            user.postValue(null)
            Log.e("debug", "failure()")

        })
    }


    fun fetchData(id: String, date1:String, date2:String){

        danielServiceRepository.getPhysicalSpacesByPersonAndTime(id, date1,date2,
            {
//                listResource.postValue(it)
                chartData.postValue(generateBarData(it))
            },
            {
                user.postValue(null)

            })

    }


    fun generateBarData(lista: List<TableFourResource>): Map<String,Long>{

        val mapDuration : MutableMap<String, Long> = mutableMapOf()
        for (element in lista){
            var aux : Long = mapDuration[element.physical_space] ?: 0
            aux += element.getDuration()
            mapDuration[element.physical_space] = aux
        }



        val sortedMap = mapDuration.toList().sortedBy { (key,value) -> value }.toMap()







        return sortedMap

    }

}