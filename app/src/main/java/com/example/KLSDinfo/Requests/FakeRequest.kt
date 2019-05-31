package com.example.KLSDinfo.Requests

import android.util.Log
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.Models.Role2
import com.example.KLSDinfo.Models.TableTwoResource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FakeRequest {




    init {
    }


    fun getRoots(): List<PhysicalSpace>{
        return listOf()
    }


    fun getAllPhysicalSpaces(strJson:String?) : List<PhysicalSpace>{
        Log.i("script", "$strJson")
        val gson = Gson()
        return if(strJson.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(strJson, object : TypeToken<List<PhysicalSpace>>() {}.type )
        }
    }


    fun getAllPersons(json:String?): MutableList<Person2>{
        val gson = Gson()

        return if(json.isNullOrBlank()){
            mutableListOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<Person2>>() {}.type )
        }

    }

    fun getAllRoles(json:String?): List<Role2>{
        val gson = Gson()

        return if(json.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<Role2>>() {}.type )
        }

    }


    fun getTableTwoData(json:String?): List<TableTwoResource>{

        val gson = Gson()

        return if(json.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<TableTwoResource>>() {}.type )
        }
    }


}