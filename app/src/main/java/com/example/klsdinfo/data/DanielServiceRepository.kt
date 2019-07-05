package com.example.klsdinfo.data

import android.app.DownloadManager
import android.os.AsyncTask
import android.util.Log
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
import com.example.klsdinfo.data.models.TableFourResource
import com.example.klsdinfo.data.models.TableThreeResource
import okhttp3.ResponseBody
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


//    fun getGroupRendezvous2(ids:String, date1:String, date2:String) : String{
//
//        var lista: String = ""
//
//        val call = DanielApiService.create().getGroupRendezvous2(ids,date1,date2)
//        call.enqueue(object : Callback<ResponseBody>{
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                lista = response.body().toString()            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })
//        return lista
//    }











}