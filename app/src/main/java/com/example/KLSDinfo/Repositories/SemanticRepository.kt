package com.example.KLSDinfo.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.Role2
import com.example.KLSDinfo.R
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


    fun getAvailablePeople(): Call<List<Person2>>{

        val service = SemanticApiService.create()

        return service.getAvailablePeople()

    }

    fun getAvailableRoles(): Call<List<Role2>>{
        val service = SemanticApiService.create()
        return service.getAvailableRoles()
    }



    fun getMultiCheckRoles2(listRoles: List<Role2>,lista: List<Person2>): List<MultiCheckRole> {
        lateinit var items: List<MultiCheckRole>
        val map : MutableMap<String,MutableList<Person2>> = mutableMapOf()
        listRoles.map {
            map[it.name] = mutableListOf()
        }
        for (person in lista) {
            for (role in person.roles!!) {
                val list = map[role.name]
                list!!.add(person)
                map[role.name] = list
            }
        }
        val multiRoles: MutableList<MultiCheckRole> = mutableListOf()
        map.map {
            multiRoles.add(MultiCheckRole(it.key, it.value, R.mipmap.ic_aluno))
        }
        items = multiRoles.filterNot {
            it.persons.isEmpty()
        } as MutableList<MultiCheckRole>
        return items
    }




}