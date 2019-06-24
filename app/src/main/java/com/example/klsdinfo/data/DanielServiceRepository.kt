package com.example.klsdinfo.data

import android.app.DownloadManager
import android.os.AsyncTask
import android.util.Log
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.GroupQuery
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
                    Log.i("retrofit", "repository on response")
                    Log.i("retrofit", "response.isSucessful =  ${response.isSuccessful}")
                    Log.i("retrofit", "response.body().isNullEmpty: ${response.body().isNullOrEmpty()}")
                    Log.i("retrofit", "response.body().isNull: ${response.body()==null}")

                    Log.i("retrofit", "response.errorBody(): ${response.errorBody()}")


                    Log.i("retrofit", "url: ${response.raw().request().url()}")



                    if(response.isSuccessful && !response.body().isNullOrEmpty()){
                        val teste = response.body()
                        success(response.body() ?: listOf())

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