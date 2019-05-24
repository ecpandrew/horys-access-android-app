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


        val stringJson2 = """
            [
  {
    "name": "UFMA",
    "description": "Universidade Federal do Maranhão",
    "holder": {
      "id": 1
    },
    "children": [
      {
        "name": "CCET",
        "description": "Centro de Ciências Exatas e Tecnologias",
        "holder": {
          "id": 2
        },
        "children": [
          {
            "name": "Prédio da Pós CCET",
            "description": "Prédio Anexo da Pós-graduaçao do CCET",
            "holder": {
              "id": 4
            },
            "children": [
              {
                "name": "LSDi",
                "description": "Laboratório de Sistemas Distribuídos Inteligentes",
                "holder": {
                  "id": 5
                },
                "children": [
                  {
                    "name": "Sala de Leitura",
                    "description": "Sala de leitura do LSDi",
                    "holder": {
                      "id": 8
                    },
                    "children": []
                  },
                  {
                    "name": "Sala dos professores",
                    "description": "Sala dos professores do LSDi",
                    "holder": {
                      "id": 9
                    },
                    "children": []
                  },
                  {
                    "name": "Salas das ETs",
                    "description": "Sala das estações de trabalho do LSDi",
                    "holder": {
                      "id": 10
                    },
                    "children": [
                      {
                        "name": "Sala do Servidor",
                        "description": "Sala do servidor do LSDi",
                        "holder": {
                          "id": 11
                        },
                        "children": []
                      },
                      {
                        "name": "Almoxarifado",
                        "description": "Almoxarifado do LSDi",
                        "holder": {
                          "id": 12
                        },
                        "children": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "LAWS",
                "description": "Laboratory of Advanced Web Systems",
                "holder": {
                  "id": 6
                },
                "children": []
              }
            ]
          },
          {
            "name": "NCA",
            "description": "Npucleo de Computação Aplicada",
            "holder": {
              "id": 7
            },
            "children": []
          }
        ]
      }
    ]
  },

  {
    "name": "Ceuma",
    "description": "Centro universitario do maranhão",
    "holder": {
      "id": 13
    },
    "children": []
  }
]
        """.trimIndent()



        val gson = Gson()

        val physicalSpaces : List<PhysicalSpace> = gson.fromJson(stringJson2, object : TypeToken<List<PhysicalSpace>>() {}.type )




        return physicalSpaces
    }






}