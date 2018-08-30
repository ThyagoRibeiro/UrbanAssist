package br.com.polieach.urbanassist.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Locality;
import br.com.polieach.urbanassist.model.User;
import br.com.polieach.urbanassist.model.WifiData;
import br.com.polieach.urbanassist.util.Constants;
import br.com.polieach.urbanassist.util.GsonUTCDateAdapter;
import br.com.polieach.urbanassist.util.HTTPRequests;
import br.com.polieach.urbanassist.view.MainActivity;
import br.com.polieach.urbanassist.view.ShowThingDetailsActivity;

/**
 * Created by Thyag on 20/07/2018.
 */

public class LocalityController {

    private static MainActivity globalShowLocalityDirectionActivity;
    private static MainActivity globalGuideToLocalityActivity;

    public static void discoverLocalityFromQRCode(int qrCodeID, final Activity activity) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("method", "qrCode");
            dataJSON.put("data", qrCodeID);

            HTTPRequests.post(dataJSON, Constants.DISCOVER_LOCALITY, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    Locality locality = gson.fromJson(response, Locality.class);
                    Intent intent = new Intent(activity, ShowThingDetailsActivity.class);
                    intent.putExtra("thing", locality);
                    activity.startActivity(intent);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void discoverLocalityFromWiFiPositioningSystem(List<ScanResult> scanResults, final Activity activity) {

        WifiData wifiData = new WifiData();

        for (ScanResult sr : scanResults) {
            wifiData.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("method", "wifiData");
            dataJSON.put("data", new JSONObject(gson.toJson(wifiData).toString()));

            HTTPRequests.post(dataJSON, Constants.DISCOVER_LOCALITY, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    Locality locality = gson.fromJson(response, Locality.class);
                    Intent intent = new Intent(activity, ShowThingDetailsActivity.class);
                    intent.putExtra("thing", locality);
                    activity.startActivity(intent);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public static void guideToLocality(Locality origin, Locality destination, User user, final boolean onlyShow, final MainActivity guideToLocalityActivity) {
//
//        try {
//            JSONObject dataJSON = new JSONObject();
//            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
//            dataJSON.put("origin", new JSONObject(gson.toJson(origin).toString()));
//            dataJSON.put("destination", new JSONObject(gson.toJson(destination).toString()));
//            dataJSON.put("user", new JSONObject(gson.toJson(user).toString()));
//            dataJSON.put("onlyShow", onlyShow);
//
//            HTTPRequests.post(dataJSON, Constants.GUIDE_TO_LOCALITY, new HTTPRequests.OKHttpNetwork() {
//                @Override
//                public void onSuccess(String response) {
//                    Edge edge = gson.fromJson(response, Edge.class);
//                    globalGuideToLocalityActivity.showDirections(edge);
//                }
//
//                @Override
//                public void onFailure() {
//
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void searchLocality(String keyword, final SearchLocalityActivity searchLocalityActivity) {
//
//        try {
//            JSONObject dataJSON = new JSONObject();
//            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
//            dataJSON.put("keyword", keyword);
//
//            HTTPRequests.post(dataJSON, Constants.SEARCH_LOCALITY, new HTTPRequests.OKHttpNetwork() {
//                @Override
//                public void onSuccess(String response) {
//
//                    ArrayList<Locality> localityList = gson.fromJson(response, new TypeToken<ArrayList<Locality>>() {
//                    }.getType());
//
//                    searchLocalityActivity.fillList(localityList);
//                }
//
//                @Override
//                public void onFailure() {
//
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
