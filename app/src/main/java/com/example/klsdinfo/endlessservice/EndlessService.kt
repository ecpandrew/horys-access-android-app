
package com.example.klsdinfo.endlessservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import br.ufma.lsdi.cddl.CDDL
import br.ufma.lsdi.cddl.Connection
import br.ufma.lsdi.cddl.ConnectionFactory
import br.ufma.lsdi.cddl.listeners.IConnectionListener
import br.ufma.lsdi.cddl.message.Message
import br.ufma.lsdi.cddl.message.ObjectFoundMessage
import br.ufma.lsdi.cddl.network.SecurityService
import br.ufma.lsdi.cddl.pubsub.Publisher
import br.ufma.lsdi.cddl.pubsub.PublisherFactory
import br.ufma.lsdi.cddl.pubsub.Subscriber
import br.ufma.lsdi.cddl.pubsub.SubscriberFactory
import com.example.klsdinfo.MainActivity
import com.example.klsdinfo.R
import com.example.klsdinfo.endlessservice.api.SemanticAPI
import com.example.klsdinfo.endlessservice.cache.DevicesCache
import com.example.klsdinfo.endlessservice.client.SemanticClient
import com.example.klsdinfo.endlessservice.models.Device
import com.example.klsdinfo.endlessservice.models.Rendezvous
import com.example.security_service.SecurityServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//import leakcanary.AppWatcher
//import leakcanary.ObjectWatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EndlessService : Service() {

//    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val APPLICATION_ID = "4e5cf492-9bee-4f96-9749-11e334c90aef"

    private var email: String? = null//"andre.cardoso@lsdi.ufma.br"
    private var MY_UUID: String? = null /* UUID of this smathphone, obtained through the user email */

    private var wakeLock: PowerManager.WakeLock? = null
    private var wakeLock2: WifiManager.WifiLock? = null

//    private var wifiLock : WifiManager.WifiLock? = null

    private var isServiceStarted = false

    private var devicesCache: DevicesCache? = null
    //    private val horysAPI = HorysClient.getClient().create(HorysAPI::class.java)
    private val semanticAPI = SemanticClient.getClient().create(SemanticAPI::class.java)


    /* IoT middleware responsible to manage bluetooth scanning */
    private  var cddl: CDDL? = null
    private  var eventSub: Subscriber? = null

    var conLocal: Connection? = null
    var conRemota: Connection? = null

    private val WINDOW_SIZE : Int = 6

    private lateinit var rendezvousWithGreatestRssi: Rendezvous
    private var currentNumberOfRendezvous : Int = 0



    // Memory leak prevention
    var macAdress : String? = null
    var publisher : Publisher? = null
    var rendezvousMessage : Message? = null
//    var mouuid: String? = null
//    var emptyMacAdress : String? = null


    override fun onBind(intent: Intent): IBinder? {
        log("Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")


        val intentEndless = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:$packageName"))
        intentEndless.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentEndless)
        if (intent != null) {
//            val action = intent.action
            val bundleEmail : String? = intent.getStringExtra("email")
            val bundleAction : String? = intent.getStringExtra("action")

//            log("using an intent with action $action")
            if(bundleEmail.isNullOrEmpty()){
                Toast.makeText(this,"Não há email, porfavor relogue", Toast.LENGTH_SHORT).show()
            }else{
                email = bundleEmail
                when (bundleAction) {
                    Actions.START.name -> startService()
                    Actions.STOP.name -> stopService()
                    else -> log("This should never happen. No action in the received intent")
                }
            }

        } else {
            log(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()

        log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)

    }



    private fun startService() {
        if (isServiceStarted) return
        log("Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }
        wakeLock2 =
            (getSystemService(Context.WIFI_SERVICE) as WifiManager).run {
                createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "EndlessService::lock2").apply {
                    acquire()
                }
            }
        init()
        cddl!!.startCommunicationTechnology(CDDL.BLE_TECHNOLOGY_ID);

//        scheduler.scheduleAtFixedRate(Runnable {
//            println("WIFI: state = ${wifiManager.wifiState}")
//            println("WIFI: isEnabled = ${wifiManager.isWifiEnabled}")
//            print(wifiManager.connectionInfo
//            )
//
//        }, 5, 5, TimeUnit.MINUTES);
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                Thread.sleep(5000)
            }
            log("End of the loop for the service")
            stop()
        }
    }

    private fun stop() {
        try {
            eventSub!!.unsubscribeAll()
            cddl!!.stopAllCommunicationTechnologies()
            cddl!!.stopService()
            conLocal!!.disconnect()
            conRemota!!.disconnect()
            CDDL.stopMicroBroker()
            stopService()
        }catch (e: Exception){
            log("Error on stopping cddl and services")
        }
    }

    private fun init() {

        getMyUUID(email?:"")
        initCache()
        initStaticRendezvous()
        configConLocal()
        configConRemota()
        initCDDL()
        initSubscriberLocal()
        initPublisherRemote()
    }
    private fun initPublisherRemote(){
        publisher = PublisherFactory.createPublisher().also {
            it.addConnection(conRemota)
        }

    }

    private fun configConLocal(){
        val host = CDDL.startSecureMicroBroker(applicationContext, true);


//        val host = CDDL.startMicroBroker();
        conLocal = ConnectionFactory.createConnection().also {
            it.clientId = "andre";
            it.host = host;
            it.addConnectionListener(connectionListenerLocal);
//            it.connect();
            it.secureConnect(applicationContext)
        }

    }
    private fun removeConLocal(){
        if (conLocal != null) {
            conLocal!!.disconnect()
        }
        if(conRemota != null) {
            conRemota!!.disconnect()
        }
        CDDL.stopMicroBroker()
    }

    private fun configConRemota(){
        val host = "192.168.15.56";
//        val host = "192.168.15.114";
        conRemota = ConnectionFactory.createConnection().also {
            it.clientId = "andre"
            it.host = host;
            it.addConnectionListener(connectionListenerRemota);
//            it.connect();
            it.secureConnect(applicationContext);
        }

    }


    private val connectionListenerLocal: IConnectionListener = object : IConnectionListener{
        val con : String = "local"
        override fun onConnectionEstablished() {
            log("Conexão $con estabelecida")
        }

        override fun onConnectionEstablishmentFailed() {
            log("Conexão $con falhou")

        }

        override fun onConnectionLost() {
            log("Conexão $con perdida")

        }

        override fun onDisconnectedNormally() {
            log("Conexão $con desconectada normalmente")

        }
    }
    private val connectionListenerRemota: IConnectionListener = object : IConnectionListener{
        val con : String = "remota"
        override fun onConnectionEstablished() {
            log("Conexão $con estabelecida")
        }

        override fun onConnectionEstablishmentFailed() {
            log("Conexão $con falhou")

        }

        override fun onConnectionLost() {
            log("Conexão $con perdida")

        }

        override fun onDisconnectedNormally() {
            log("Conexão $con desconectada normalmente")

        }
    }

    private fun initStaticRendezvous(){
        rendezvousWithGreatestRssi = Rendezvous("","","",0.0,0.0,Double.NEGATIVE_INFINITY,0)
    }

    private fun getMyUUID(email: String) {
        val call = semanticAPI.getUserDevices(email)
        call.enqueue(object : Callback<List<Device>>{
            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                val deviceList = response.body()
                if(deviceList.isNullOrEmpty()){
                    MY_UUID = null
                    log("init() failed: user not exists")
                }else{
                    MY_UUID = deviceList[0].uuid
                    log("--------- User found: $email")
                }
            }
            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                log("init() failed: request httl failed")
            }
        })
    }

    private fun initSubscriberLocal() {
        val sub : Subscriber = SubscriberFactory.createSubscriber()
        sub.addConnection(conLocal)
        sub.subscribeObjectFoundTopic()
        sub.setSubscriberListener {
            when(it){
                is ObjectFoundMessage -> {
                    macAdress = getMacAddress(it)
                    if(macAdress != null){
                        if(isRegistered(macAdress!!)){
                            fowardRendezvous(MY_UUID!!, macToUUID(macAdress!!), it.signal)
                        }
                    }
                }
            }
        }
    }


    private fun postRendezvous(myUuid: String, thingID: String, rssi: Double) {
//        rendezvousMessage = RendezvousMessage().also {
//            it.appID = UUID.fromString(APPLICATION_ID)
//            it.mhubID = UUID.fromString(myUuid)
//            it.thingID = UUID.fromString(thingID)
//            it.signal = rssi
//            it.latitude = 0.0
//            it.longitude = 0.0
//            it.timestamp = System.currentTimeMillis() / 1000L
//            it.serviceName = "rendezvous"
//        }
        println(">>>>>>>>>>> POSTADO")

        rendezvousMessage = Message().also {
            it.payload = "$APPLICATION_ID;$myUuid;$thingID;$rssi".toByteArray()
            it.serviceName = "rendezvous"
        }
        publisher.run {
            this?.publish(rendezvousMessage)
        }

    }


    private fun fowardRendezvous(myUuid: String, thingID: String, rssi: Double) {

        if (currentNumberOfRendezvous < WINDOW_SIZE){

            currentNumberOfRendezvous +=1

            if(rssi > rendezvousWithGreatestRssi.signal){
                rendezvousWithGreatestRssi.signal = rssi
                rendezvousWithGreatestRssi.thingID = thingID
            }
        }
        if(currentNumberOfRendezvous >= WINDOW_SIZE){
            postRendezvous(myUuid, rendezvousWithGreatestRssi.thingID, rendezvousWithGreatestRssi.signal)
            rendezvousWithGreatestRssi.signal = Double.NEGATIVE_INFINITY
            currentNumberOfRendezvous = 0

        }


    }




    private fun macToUUID(macAdress: String): String {
        return devicesCache!!.getUUID(macAdress)
    }



    private fun isRegistered(macAdress: String): Boolean {
        return devicesCache!!.isRegistered(macAdress)
    }

    private fun getMacAddress(ofm: ObjectFoundMessage): String? {
        if(ofm.mouuid != null && ofm.mouuid!!.length > 2){
            return ofm.mouuid!!.substring(2)
        }
        return null

    }

    private fun stopCDDL(){
        cddl = CDDL.getInstance();
        cddl!!.stopCommunicationTechnology(CDDL.BLE_TECHNOLOGY_ID)
        cddl!!.stopService()

    }

    private fun initCDDL() {
        try {
            cddl = CDDL.getInstance();
            if(cddl != null) {
                cddl!!.connection = conLocal;
                cddl!!.context = this;
                cddl!!.startService();
                //cddl.startLocationSensor();
//                cddl!!.setQoS(TimeBasedFilterQoS());
                println("---------------- CDDL iniciado ------------")
            }

        }catch (e : Exception){
            log("initCDDL() Failed")
            e.message?.let { log(it) }
        }


    }


    private fun initCache() {

        devicesCache = DevicesCache.getInstance()

        val call = semanticAPI.devices

        call.enqueue(object: Callback<List<Device>>{

            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                val deviceList = response.body()
                log(deviceList.toString())
                devicesCache!!.update(deviceList!!)
            }

            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                log("initCache() failed: error on request")

            }

        })

    }

    private fun stopService() {
        log("Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            wakeLock2?.let {
                if (it.isHeld) {
                    it.release()
                }
            }

//            wifiLock?.let{
//                if(it.isHeld){
//                    it.release()
//                }
//            }

//            scheduler.shutdown()
            stop()
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            log("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }



    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }



}


