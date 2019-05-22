package com.example.KLSDinfo.Requests

import com.example.KLSDinfo.Models.PhysicalSpace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FakeRequest {




    init {
    }

    fun getAllPhysicalSpaces() : List<PhysicalSpace>{

        val stringJson = """
            [
              {
                "name": "UFMA",
                "description": "Univerdidade Federal do Maranhão",
                "holder": {
                  "id": 8
                },
                "children": [
                  {
                    "name": "LSDi",
                    "description": "Laboratório de Sistemas Distribuídos Inteligentes",
                    "holder": {
                      "id": 14
                    },
                    "children": []
                  }
                ]
              },
              {
                "name": "LSDi",
                "description": "Laboratório de Sistemas Distribuídos Inteligentes",
                "holder": {
                  "id": 14
                },
                "children": []
              }
            ]
        """.trimIndent()

        val gson = Gson()

        val physicalSpaces : List<PhysicalSpace> = gson.fromJson(stringJson, object : TypeToken<List<PhysicalSpace>>() {}.type )




        return physicalSpaces
    }






}