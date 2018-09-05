package br.com.polieach.urbanassist.model;

import java.util.Date;
import java.util.HashMap;

public class WifiData {

    private Date date;
    private int idThing;
    private HashMap<String, Integer> wifiMap;

    public WifiData() {
        wifiMap = new HashMap<>();
        date = new Date();
    }

    public int get(String bssid) {
        return wifiMap.get(bssid);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIDThing() {
        return idThing;
    }

    public void setIDThing(int qrCodeID) {
        this.idThing = qrCodeID;
    }

    public HashMap<String, Integer> getWifiMap() {
        return wifiMap;
    }

    public void setWifiMap(HashMap<String, Integer> wifiMap) {
        this.wifiMap = wifiMap;
    }

    public void put(String bssid, int rssi) {
        wifiMap.put(bssid, rssi);
    }

}