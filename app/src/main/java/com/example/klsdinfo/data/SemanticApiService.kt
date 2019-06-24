package com.example.klsdinfo.data

import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.PhysicalSpace
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SemanticApiService {

    @GET("persons")
   fun getAvailablePeople(): Call< List<Person2>>





    @GET("physical_spaces/roots")
    fun getPhysicalSpaces(): Call<List<PhysicalSpace>>




    companion object Factory {

        fun create(): SemanticApiService {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://smartlab.lsdi.ufma.br/semantic/api/").build()
            return retrofit.create(SemanticApiService::class.java)
        }
    }




}

