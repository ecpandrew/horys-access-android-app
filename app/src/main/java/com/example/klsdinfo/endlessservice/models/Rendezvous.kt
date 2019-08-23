package com.example.klsdinfo.endlessservice.models

data class Rendezvous(
    private var appID : String,
    private var mhubID : String,
    private var thingID : String,
    private var latitude : Double,
    private var longitude : Double,
    private var signal : Double,
    private var timestamp : Long
)