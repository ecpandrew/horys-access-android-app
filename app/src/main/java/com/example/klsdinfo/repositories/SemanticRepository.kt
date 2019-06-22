package com.example.klsdinfo.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.klsdinfo.models.MultiCheckRole
import com.example.klsdinfo.models.Person2
import com.example.klsdinfo.models.Role2
import com.example.klsdinfo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SemanticRepository private constructor(private val semanticService: SemanticApiService) {




    companion object{

        @Volatile private var instance: SemanticRepository? = null

        fun getInstance(semanticService: SemanticApiService) = instance?: synchronized(this){
            instance ?: SemanticRepository(semanticService).also { instance = it }

        }


    }

    private var cached: List<Person2> = listOf()

    fun getAvailablePeople( success: (List<Person2>) -> Unit, failure: () -> Unit ) {

        if(cached.isNullOrEmpty()){
            Log.i("retrofit","people request done")

            val call = SemanticApiService.create().getAvailablePeople()
            call.enqueue(object : Callback<List<Person2>> {
                override fun onResponse(call: Call<List<Person2>>, response: Response<List<Person2>>) {


                    if(response.isSuccessful && !response.body().isNullOrEmpty()){
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








}