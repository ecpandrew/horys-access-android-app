package com.example.KLSDinfo.Repositories

import com.example.KLSDinfo.Models.MultiCheckRole

data class MCache(val lista : List<MultiCheckRole>?){

    fun getData() : List<MultiCheckRole> {
        return if(lista!= null){
            lista
        }else{
            listOf()
        }
    }
}
