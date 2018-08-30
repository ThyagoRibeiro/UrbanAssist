package br.com.polieach.urbanassist.controller;

import android.net.wifi.ScanResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.WifiData;
import br.com.polieach.urbanassist.util.Constants;
import br.com.polieach.urbanassist.util.GsonUTCDateAdapter;
import br.com.polieach.urbanassist.util.HTTPRequests;

/**
 * Created by Thyag on 20/07/2018.
 */

public class WifiDataController {

    public static void saveWifiData(Thing thing, List<ScanResult> scanResults){

        WifiData wifiData = new WifiData();

        for (ScanResult sr : scanResults) {
            wifiData.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        wifiData.setIDThing(thing.getID());

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("wifiData",  new JSONObject(gson.toJson(wifiData).toString()));

            HTTPRequests.post(dataJSON, Constants.SAVE_WIFI_DATA, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
