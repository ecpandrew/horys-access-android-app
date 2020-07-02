//package com.example.data;
//
//import java.io.Serializable;
//import java.util.UUID;
//
//import br.ufma.lsdi.cddl.message.Message;
//
//public class RendezvousMessage extends Message {
////    private static final long serialVersionUID = 5050301321863757269L;
//    private static final long serialVersionUID = 5050301321863757269L;
//
//    private UUID appID;
//    private UUID mhubID;
//    private UUID thingID;
//    private long timestamp;
//    private double latitude;
//    private double longitude;
//    private double rendezvousSignal;
//
//    public RendezvousMessage(){
//
//    }
//
//    public RendezvousMessage(
//            UUID appID,
//            UUID mhubID,
//            UUID thingID,
//            long timestamp,
//            double latitude,
//            double longitude,
//            double rendezvousSignal) {
//        super();
//        this.appID = appID;
//        this.mhubID = mhubID;
//        this.thingID = thingID;
//        this.timestamp = timestamp;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.rendezvousSignal = rendezvousSignal;
//        setQocEvaluated(true);
//    }
//
//
//    public UUID getAppID() {
//        return appID;
//    }
//
//    public UUID getMhubID() {
//        return mhubID;
//    }
//
//    public UUID getThingID() {
//        return thingID;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public double getRendezvousSignal() {
//        return rendezvousSignal;
//    }
//
//    public void setAppID(UUID appID) {
//        this.appID = appID;
//    }
//
//    public void setMhubID(UUID mhubID) {
//        this.mhubID = mhubID;
//    }
//
//    public void setThingID(UUID thingID) {
//        this.thingID = thingID;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
//    public void setRendezvousSignal(double signal) {
//        this.rendezvousSignal = signal;
//    }
//}