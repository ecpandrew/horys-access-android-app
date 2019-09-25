package com.example.klsdinfo.data

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.klsdinfo.data.models.TableFourResource
import com.example.klsdinfo.data.models.TableTwoResource


class HomeViewModel(

    private val semanticRepository: SemanticRepository,  private val danielServiceRepository: DanielServiceRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{


    val loadingProgress = MutableLiveData<Boolean>().apply { value = true }

    var error = MutableLiveData<Pair<Int,String>>().apply { value = Pair(100,"Fetching: user") }

//    val user = MutableLiveData<Person2?>().apply { value = null }
    var email = MutableLiveData<String>().apply{ value = ""}


    val date1 = MutableLiveData<String>().apply { value = "" }

    val date2 = MutableLiveData<String>().apply { value = "" }

    val listResource = MutableLiveData<List<TableFourResource>>().apply { value = mutableListOf() }

    val chartData = MutableLiveData<Map<String,Long>>().apply { value = mapOf() }

    val currentPosition = MutableLiveData<List<TableTwoResource>>().apply { value = mutableListOf() }

    val listData = MutableLiveData<List<String>>().apply { value = mutableListOf() }



    fun setDates(unixTime:String, unixTimePast: String){
        date1.value = unixTime
        date2.value = unixTimePast
    }


    fun generateListViewData(lista : List<TableTwoResource>){

        val result = mutableListOf<String>()

        for(element in lista){
            result.add("${element.physical_space} -> ${element.duration/60} minutes.")
        }


        listData.postValue(result)
    }




    fun fetchCurrentPosition(id: String){

        danielServiceRepository.getCurrentPosition(id,
            {
                if(it.isEmpty()){

                    //Todo()
                }else{
                    currentPosition.postValue(it)
                    generateListViewData(it)
                }

            },
            {

                currentPosition.postValue(null)
//                TODO() //this might cause a bug

            })


    }

    fun fetchUserForCurrentPosition(){

        semanticRepository.getUserFromRoom({

            fetchCurrentPosition(it.holder.id.toString())

        }, {

//            // Todo()
//            currentPosition.postValue(null)


        })
    }

    fun fetchUserForChart(){

        semanticRepository.getUserFromRoom({


            error.postValue(Pair(210, "Success: user found"))
            fetchData(it.holder.id.toString(), date1.value!!, date2.value!!)


        }, {


            when(it){
                504 -> error.postValue(Pair(504, "Error: internal room error, please report BUG!"))

                503 -> error.postValue(Pair(504, "Error: semantic server may be down, please report!"))
            }


            chartData.postValue(null)


        })
    }


    fun fetchData(id: String, date1:String, date2:String){

        error.postValue(Pair(101, "Fetching: user data"))

        danielServiceRepository.getPhysicalSpacesByPersonAndTime(id, date1,date2,
            {

                chartData.postValue(generateBarData(it))
                error.postValue(Pair(200, "Success: data is consistent"))


            },
            {

                chartData.postValue(null)
                error.postValue(Pair(505, "Error: your connection or AttendanceService is down, please report!"))

                //TODO //this might cause a bug

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