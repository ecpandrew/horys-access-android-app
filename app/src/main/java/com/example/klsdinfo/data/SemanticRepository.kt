package com.example.klsdinfo.data

import android.util.Log
import com.example.klsdinfo.data.models.Location
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SemanticRepository private constructor(private val semanticService: SemanticApiService) {


    companion object{
        @Volatile private var instance: SemanticRepository? = null
        fun getInstance(semanticService: SemanticApiService) = instance ?: synchronized(this){
            instance ?: SemanticRepository(semanticService).also { instance = it }
        }
    }


    private var cached: List<Person2> = listOf()




    fun getAvailablePeople(success: (List<Person2>) -> Unit, failure: () -> Unit ) {

        if(cached.isNullOrEmpty()){
            Log.i("retrofit","people request done")

            val call = SemanticApiService.create().getAvailablePeople()
            call.enqueue(object : Callback<List<Person2>> {
                override fun onResponse(call: Call<List<Person2>>, response: Response<List<Person2>>) {
                    if(response.isSuccessful){
                        cached = response.body()!!
                        success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<Person2>>, t: Throwable) {
                    failure()
                }
            })
        }else{
            Log.i("retrofit","cache used")
            success(cached)
        }
    }

    private var cached_locations: List<PhysicalSpace> = listOf()


    fun getPhysicalSpaces(success: (List<PhysicalSpace>) -> Unit, failure: () -> Unit){

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