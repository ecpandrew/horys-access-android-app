package com.example.klsdinfo.endlessservice.models

data class Rendezvous(
    var appID : String,
    var mhubID : String,
    var thingID : String,
    var latitude : Double,
    var longitude : Double,
    var signal : Double,
    var timestamp : Long
)