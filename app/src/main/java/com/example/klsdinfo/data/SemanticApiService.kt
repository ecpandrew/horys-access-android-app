package com.example.klsdinfo.data

import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace
import com.example.klsdinfo.data.models.TableThreeResource
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SemanticApiService {

    @GET("persons")
   fun getAvailablePeople(): Call<List<Person2>>





    @GET("physical_spaces/roots")
    fun getPhysicalSpaces(): Call<List<PhysicalSpace>>


    @GET("persons/{email}")
    fun getUser(@Path("email") email: String): Call<Person2>



    companion object Factory {

        fun create(): SemanticApiService {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://192.168.15.144:5002/api/").build()
            return retrofit.create(SemanticApiService::class.java)
        }
    }




}

