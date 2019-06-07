package com.example.KLSDinfo.Models

import android.util.Log
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
    fun getTableFourData(json:String?): List<TableFourResource>{

        val gson = Gson()

        return if(json.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<TableFourResource>>() {}.type )
        }
    }
    fun getTableFiveData(json:String?): List<TableFiveResource>{

        val gson = Gson()

        return if(json.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<TableFiveResource>>() {}.type )
        }
    }


    fun getTableThreeData(json:String?): List<TableThreeResource>{

        val gson = Gson()

        return if(json.isNullOrBlank()){
            listOf()
        }else {
            gson.fromJson(json, object : TypeToken<List<TableThreeResource>>() {}.type )
        }
    }

}