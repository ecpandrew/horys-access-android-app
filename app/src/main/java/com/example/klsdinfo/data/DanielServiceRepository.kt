package com.example.klsdinfo.data

import android.os.AsyncTask
import android.util.Log
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DanielServiceRepository private constructor(private val danielService: DanielApiService, private val database: AppDatabase) {


    companion object{
        @Volatile private var instance: DanielServiceRepository? = null

        fun getInstance(danielService: DanielApiService, appDatabase: AppDatabase) = instance ?: synchronized(this){
            instance ?: DanielServiceRepository(danielService, appDatabase).also { instance = it }
        }


    }



    fun getConnectedPeople(success: (List<TableOneResource>) -> Unit, failure: () -> Unit){
        Log.i("retrofit", "repository init")
        AsyncTask.execute {
            Log.i("retrofit", "async init")
            val query: GroupQuery = database.groupDao().getAll()[0]
            Log.i("retrofit", "query: ${database.groupDao().getAll().size}")
            val call = DanielApiService.create().getConnectedPeople(query.ids.toString().trim())
            call.enqueue(object : Callback<List<TableOneResource>>{
                override fun onResponse(call: Call<List<TableOneResource>>, response: Response<List<TableOneResource>>
                ) {
                    Log.i("timestamp", "url usada: ${response.raw().request().url()}")
                    if(response.isSuccessful){
                        if(response.body().isNullOrEmpty()) success(listOf())
                        else success(response.body()!!)
                        Log.i("retrofit", "repository list ${response.body()}")
                    }
                }
                override fun onFailure(call: Call<List<TableOneResource>>, t: Throwable) {
                    Log.i("retrofit", "repository on failure")
                    failure()
                }
            })
        }
    }



    fun getPhysicalSpaces(success: (List<TableTwoResource>) -> Unit, failure: () -> Unit){
        Log.i("retrofit", "repository init")
        AsyncTask.execute {
            Log.i("retrofit", "async init")
            val query: GroupQuery = database.groupDao().getAll()[0]
            Log.i("retrofit", "query: ${database.groupDao().getAll().size}")
            val call = DanielApiService.create().getPhysicalSpaces(query.ids.toString().trim())
            call.enqueue(object : Callback<List<TableTwoResource>>{
                override fun onResponse(call: Call<List<TableTwoResource>>, response: Response<List<TableTwoResource>>
                ) {
                    Log.i("timestamp", "url usada: ${response.raw().request().url()}")
                    if(response.isSuccessful){
                        if(response.body().isNullOrEmpty()) success(listOf())
                        else success(response.body()!!)
                        Log.i("retrofit", "repository list ${response.body()}")
                    }
                }
                override fun onFailure(call: Call<List<TableTwoResource>>, t: Throwable) {
                    Log.i("retrofit", "repository on failure")
                    failure()
                }
            })
        }
    }












    fun getPhysicalSpacesByPersonAndTime(id:String, date1: String, date2:String, success: (List<TableFourResource>) -> Unit, failure : () -> Unit ){
        val call = danielService.getPhysicalSpacesByPersonAndTime(id,date2,date1)
        call.enqueue(object : Callback<List<TableFourResource>>{
            override fun onResponse(call: Call<List<TableFourResource>>, response: Response<List<TableFourResource>>) {

                Log.i("dataurl", "url usada: ${response.raw().request().url()}")

                if(response.isSuccessful){
                    if(response.body().isNullOrEmpty()) success(listOf())
                    else success(response.body()!!)

                    Log.i("retrofit", "repository list ${response.body()}")
                }            }
            override fun onFailure(call: Call<List<TableFourResource>>, t: Throwable) {
                failure()
            }
        })
    }





    fun getGroupRendezvous(success: (List<TableThreeResource>) -> Unit, failure: () -> Unit){
        Log.i("retrofit", "repository init")
        AsyncTask.execute {
            Log.i("retrofit", "async init")
            val query: GroupQuery = database.groupDao().getAll()[0]
            Log.i("retrofit", "query: ${database.groupDao().getAll().size}")
            val call = DanielApiService.create().getGroupRendezvous2(query.ids.toString().trim(),query.pastDate.toString().trim(),query.currentDate.toString().trim())
            call.enqueue(object : Callback<List<TableThreeResource>>{
                override fun onResponse(call: Call<List<TableThreeResource>>, response: Response<List<TableThreeResource>>
                ) {
                    Log.i("timestamp", "url usada: ${response.raw().request().url()}")
                    if(response.isSuccessful){
                        if(response.body().isNullOrEmpty()) success(listOf())
                        else success(response.body()!!)
                        Log.i("retrofit", "repository list ${response.body()}")
                    }
                }
                override fun onFailure(call: Call<List<TableThreeResource>>, t: Throwable) {
                    Log.i("retrofit", "repository on failure")
                    failure()
                }
            })
        }
    }






    fun getPeopleHistory(success: (List<TableFourResource>) -> Unit, failure: () -> Unit){
        Log.i("retrofit", "repository init")
        AsyncTask.execute {

            Log.i("retrofit", "async init")
            val query: GroupQuery = database.groupDao().getAll()[0]
            Log.i("retrofit", "query: ${database.groupDao().getAll().size}")
            val call = DanielApiService.create().getPhysicalSpacesByPersonAndTime(query.ids.toString().trim(),query.pastDate.toString().trim(),query.currentDate.toString().trim())

            call.enqueue(object : Callback<List<TableFourResource>>{
                override fun onResponse(call: Call<List<TableFourResource>>, response: Response<List<TableFourResource>>
                ) {
                    Log.i("timestamp", "url usada: ${response.raw().request().url()}")
                    if(response.isSuccessful){
                        if(response.body().isNullOrEmpty()) success(listOf())
                        else success(response.body()!!)
                        Log.i("retrofit", "repository list ${response.body()}")
                    }
                }
                override fun onFailure(call: Call<List<TableFourResource>>, t: Throwable) {
                    Log.i("retrofit", "repository on failure")
                    failure()
                }
            })
        }

    }



    fun getPhysicalSpaceHistory(success: (List<TableFiveResource>) -> Unit, failure: () -> Unit){
        Log.i("retrofit", "repository init")
        AsyncTask.execute {

            Log.i("retrofit", "async init")
            val query: GroupQuery = database.groupDao().getAll()[0]
            Log.i("retrofit", "query: ${database.groupDao().getAll().size}")
            val call = DanielApiService.create().getPhysicalSpaceHistory(query.ids.toString().trim(),query.pastDate.toString().trim(),query.currentDate.toString().trim())

            call.enqueue(object : Callback<List<TableFiveResource>>{
                override fun onResponse(
                    call: Call<List<TableFiveResource>>,
                    response: Response<List<TableFiveResource>>
                ) {
                    Log.i("timestamp", "url usada: ${response.raw().request().url()}")
                    if(response.isSuccessful){
                        if(response.body().isNullOrEmpty()) success(listOf())
                        else success(response.body()!!)
                        Log.i("retrofit", "repository list ${response.body()}")
                    }                }

                override fun onFailure(call: Call<List<TableFiveResource>>, t: Throwable) {
                    Log.i("retrofit", "repository on failure")
                    failure()
                }
            })
        }

    }







}