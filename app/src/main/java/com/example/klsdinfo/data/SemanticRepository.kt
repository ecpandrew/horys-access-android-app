package com.example.klsdinfo.data

import android.os.AsyncTask
import android.util.Log
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.LocalUserQuery
import com.example.klsdinfo.data.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SemanticRepository private constructor(private val semanticService: SemanticApiService, private val database: AppDatabase) {


    companion object{
        @Volatile private var instance: SemanticRepository? = null
        fun getInstance(semanticService: SemanticApiService, appDatabase: AppDatabase) = instance ?: synchronized(this){
            instance ?: SemanticRepository(semanticService, appDatabase).also { instance = it }
        }
    }


    private var cached: List<Person2> = listOf()

    private var userCached: Person2? = null






    fun getUserFromRoom(success:(Person2) -> Unit, failure: (Int) -> Unit){



        AsyncTask.execute {

            val query: LocalUserQuery? = database.localUserDao().getAll()[0]



            if(query == null){
                failure(504)
            }

            val call = SemanticApiService.create().getUser(query!!.email)

            call.enqueue(object : Callback<Person2>{

                override fun onResponse(call: Call<Person2>, response: Response<Person2>) {

                    Log.i("retrofit", "url usada: ${response.raw().request().url()}")


                    if (response.isSuccessful) {
                        userCached = response.body()!!
                        success(response.body()!!)
                        Log.i("retrofit", "success: ${response.body()!!}")
                    }else{
                        failure(503)

                    }
                }

                override fun onFailure(call: Call<Person2>, t: Throwable) {
                    failure(503)
                    Log.i("retrofit", "failure: ${t.message}")

                }
            })
        }





//        if(userCached == null){
//
//            val call = SemanticApiService.create().getUser("andreluizalmeidacardoso@gmail.com")
//            call.enqueue(object : Callback<Person2> {
//                override fun onResponse(call: Call<Person2>, response: Response<Person2>) {
//                    if(response.isSuccessful){
//                        userCached = response.body()!!
//                        success(response.body()!!)
//                    }
//                }
//                override fun onFailure(call: Call<Person2>, t: Throwable) {
//                    failure()
//                }
//            })
//
//
//        }else{
//            success(userCached!!)
//        }



    }


    fun getAvailablePeople(success: (List<Person2>) -> Unit, failure: () -> Unit ) {

        if(cached.isNullOrEmpty()){
            Log.i("retrofit33","people request done")

            val call = SemanticApiService.create().getAvailablePeople()
            call.enqueue(object : Callback<List<Person2>> {
                override fun onResponse(call: Call<List<Person2>>, response: Response<List<Person2>>) {
                    if(response.isSuccessful){
                        Log.i("retrofit33","people request done + ${response.body().isNullOrEmpty()}")
                        cached = response.body()!!
                        success(response.body()!!)

                    }
                    Log.i("retrofit33","people request done + response not sucessfull")

                }
                override fun onFailure(call: Call<List<Person2>>, t: Throwable) {
                    failure()
                }
            })
        }else{
            Log.i("retrofit33","cache used")
            success(cached)
        }
    }

    private var cached_locations: List<PhysicalSpace> = listOf()


    fun getPhysicalSpaces(success: (List<PhysicalSpace>) -> Unit, failure: () -> Unit){
        Log.i("retrofit","physical spaces request done")

        if(cached_locations.isNullOrEmpty()){
            val call = SemanticApiService.create().getPhysicalSpaces()
            call.enqueue(object: Callback<List<PhysicalSpace>>{

                override fun onResponse(call: Call<List<PhysicalSpace>>, response: Response<List<PhysicalSpace>>) {
                    if(response.isSuccessful && !response.body().isNullOrEmpty()){
                        cached_locations = response.body()!!
                        success(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<List<PhysicalSpace>>, t: Throwable) {
                    failure()

                }
            })

        }else{
            Log.i("retrofit","physical space's cache used")

            success(cached_locations)
        }

    }








}