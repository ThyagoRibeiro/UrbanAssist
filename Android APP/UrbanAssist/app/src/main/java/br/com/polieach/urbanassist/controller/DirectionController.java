package br.com.polieach.urbanassist.controller;

import android.net.wifi.ScanResult;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.User;
import br.com.polieach.urbanassist.model.WifiData;
import br.com.polieach.urbanassist.util.Constants;
import br.com.polieach.urbanassist.util.GsonUTCDateAdapter;
import br.com.polieach.urbanassist.util.HTTPRequests;
import br.com.polieach.urbanassist.view.MainActivity;

/**
 * Created by Thyag on 20/07/2018.
 */

public class DirectionController {

    public static void showDirectionFromQRCode(int qrCodeID, User user, final MainActivity mainActivity) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

            dataJSON.put("method", "qrCode");
            dataJSON.put("data", qrCodeID);
            dataJSON.put("user", new JSONObject(gson.toJson(user).toString()));

            Log.d("showDirectionFromQRCcde", dataJSON.toString());

            HTTPRequests.post(dataJSON, Constants.SHOW_DIRECTION, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    Log.d("response", response);
                    if (response.contains("success")) {
                        mainActivity.reachedTheGoal();
                    } else {
                        Edge edge = gson.fromJson(response, Edge.class);
                        mainActivity.updateNextDirection(edge);
                    }
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void showDirectionFromWiFiPositioningSystem(List<ScanResult> scanResults, User user, final MainActivity mainActivity) {

        WifiData wifiData = new WifiData();

        for (ScanResult sr : scanResults) {
            wifiData.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("method", "wifiData");
            dataJSON.put("data", new JSONObject(gson.toJson(wifiData).toString()));

            HTTPRequests.post(dataJSON, Constants.SHOW_DIRECTION, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    Log.d("response", response);
                    if (response.contains("success")) {
                        mainActivity.reachedTheGoal();

                    } else {
                        Edge edge = gson.fromJson(response, Edge.class);
                        mainActivity.updateNextDirection(edge);
                    }
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
