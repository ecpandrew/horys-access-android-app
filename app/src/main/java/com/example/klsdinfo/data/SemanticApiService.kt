package com.example.klsdinfo.data

import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.Role2
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SemanticApiService {

    @GET("persons")
   fun getAvailablePeople(): Call< List<Person2> >




    @GET("roles")
    fun getAvailableRoles(): Call<List<Role2>>

    companion object Factory {

        fun create(): SemanticApiService {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://smartlab.lsdi.ufma.br/semantic/api/").build()
            return retrofit.create(SemanticApiService::class.java)
        }
    }

}

