package com.example.klsdinfo.repositories

import com.example.klsdinfo.models.MultiCheckRole

data class MCache(val lista : List<MultiCheckRole>?){

    fun getData() : List<MultiCheckRole> {
        return if(lista!= null){
            lista
        }else{
            listOf()
        }
    }
}
