package br.com.polieach.urbanassist.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;
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

public class ThingController {

    private static MainActivity globalShowThingDirectionActivity;
    private static MainActivity globalGuideToThingActivity;

    public static void discoverThingFromID(int qrCodeID, final MainActivity mainActivity) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("method", "qrCode");
            dataJSON.put("data", qrCodeID);

            HTTPRequests.post(dataJSON, Constants.DISCOVER_THING, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    Thing thing = gson.fromJson(response, Thing.class);
                    mainActivity.showDetailsView(thing);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void discoverThingFromWiFiPositioningSystem(List<ScanResult> scanResults, final Activity activity) {

        WifiData wifiData = new WifiData();

        for (ScanResult sr : scanResults) {
            wifiData.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("method", "wifiData");
            dataJSON.put("data", new JSONObject(gson.toJson(wifiData).toString()));

            HTTPRequests.post(dataJSON, Constants.DISCOVER_THING, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    Thing thing = gson.fromJson(response, Thing.class);
                    Intent intent = new Intent(activity, ShowThingDetailsActivity.class);
                    intent.putExtra("thing", thing);
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

    public static void guideToThing(Thing origin, Thing destination, User user, final MainActivity mainActivity) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("origin", new JSONObject(gson.toJson(origin).toString()));
            dataJSON.put("destination", new JSONObject(gson.toJson(destination).toString()));
            dataJSON.put("user", new JSONObject(gson.toJson(user).toString()));

            Log.d("log", dataJSON.toString());

            HTTPRequests.post(dataJSON, Constants.GUIDE_TO_THING, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    ArrayList<Edge> edgeList = gson.fromJson(response, new TypeToken<ArrayList<Edge>>() {
                    }.getType());

                    MainActivity.showDirections(edgeList);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void searchThing(String keyword, final MainActivity mainActivity) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("keyword", keyword);

            HTTPRequests.post(dataJSON, Constants.SEARCH_THING, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    Log.d("response searchThing", response);

                    ArrayList<Thing> thingList = gson.fromJson(response, new TypeToken<ArrayList<Thing>>() {
                    }.getType());

                    mainActivity.fillList(thingList);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void discoverNeighbours(Thing origin, int NEIGHBOUR_DEPTH, final MainActivity mainActivity) {
        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("thing", new JSONObject(gson.toJson(origin).toString()));
            dataJSON.put("neighbour_depth", NEIGHBOUR_DEPTH);

            HTTPRequests.post(dataJSON, Constants.DISCOVER_NEIGHBOURS, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    if(response.contains("error")) {

                        ArrayList<Edge> edgeList = gson.fromJson(response, new TypeToken<ArrayList<Edge>>() {
                        }.getType());

                        Log.d("response showRoute", "" + edgeList.size());
                        mainActivity.showDirections(edgeList);
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
