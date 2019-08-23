package com.example.klsdinfo.endlessservice.cache;


import com.example.klsdinfo.endlessservice.models.Device;

import java.util.Hashtable;
import java.util.List;

public class DevicesCache {
    private static DevicesCache instance;

    private Hashtable<String, String> registeredDevices;

    private DevicesCache() {
        registeredDevices = new Hashtable<>();
    }

    public static DevicesCache getInstance() {
        if (instance == null)
            instance = new DevicesCache();
        return instance;
    }

    public int debug(){
        return registeredDevices.size();
    }

    public void update(List<Device> deviceList) {
        registeredDevices.clear();

        for (Device device : deviceList) {
            if (device.getMacAddress() != null) {
                String macAddress = device.getMacAddress();
                String uuid       = device.getUuid();

                macAddress = normalizeMac(macAddress);

                registeredDevices.put(macAddress, uuid);
            }
        }
    }

    public boolean isRegistered(String macAddress) {
        macAddress = normalizeMac(macAddress);
        return registeredDevices.containsKey(macAddress);
    }

    public String getUUID(String macAddress) {
        macAddress = normalizeMac(macAddress);
        return registeredDevices.get(macAddress);
    }

    private String normalizeMac(String macAddress) {
        String newMac = macAddress.replace(":", "");

        newMac = newMac.toUpperCase();

        return newMac;
    }

}