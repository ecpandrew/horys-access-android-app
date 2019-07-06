package com.example.klsdinfo.data

import com.example.klsdinfo.data.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface DanielApiService {





    @GET("physical_spaces/{ids}persons")
    fun getConnectedPeople(@Path("ids", encoded = true) ids : String): Call<List<TableOneResource>>


    @GET("persons/{ids}/physical_spaces/")
    fun getPhysicalSpaces(@Path("ids", encoded = true) ids : String): Call<List<TableTwoResource>>





    @GET("persons/{ids}rendezvous/{pastDate}/{currentDate}/")
    fun getGroupRendezvous2(  @Path("ids", encoded = true) ids : String , @Path("pastDate") date1: String, @Path("currentDate") date2: String): Call<List<TableThreeResource>>



    @GET("persons/{ids}/physical_spaces/{date1}/{date2}/")
    fun getPhysicalSpacesByPersonAndTime(@Path("ids", encoded = true) ids : String, @Path("date1") date1: String, @Path("date2") date2: String): Call<List<TableFourResource>>



    @GET("physical_spaces/{ids}persons/{date1}/{date2}/")
    fun getPhysicalSpaceHistory(@Path("ids", encoded = true) ids : String, @Path("date1") date1: String, @Path("date2") date2: String): Call<List<TableFiveResource>>







    companion object Factory {

        fun create(): DanielApiService {
            val retrofit = Retrofit.Builder().baseUrl("http://smartlab.lsdi.ufma.br/service/").addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(DanielApiService::class.java)
        }
    }




}

