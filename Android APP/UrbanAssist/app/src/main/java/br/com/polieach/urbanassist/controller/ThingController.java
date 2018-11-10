package br.com.polieach.urbanassist.controller;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.ThingProbability;
import br.com.polieach.urbanassist.model.User;
import br.com.polieach.urbanassist.model.WifiData;
import br.com.polieach.urbanassist.util.Constants;
import br.com.polieach.urbanassist.util.GsonUTCDateAdapter;
import br.com.polieach.urbanassist.util.HTTPRequests;
import br.com.polieach.urbanassist.view.DirectionsActivity;
import br.com.polieach.urbanassist.view.ListAdapter;
import br.com.polieach.urbanassist.view.MainActivity;
import br.com.polieach.urbanassist.view.RowData;

/**
 * Created by Thyag on 20/07/2018.
 */

public class ThingController {

    private static MainActivity globalShowThingDirectionActivity;
    private static MainActivity globalGuideToThingActivity;

//    public static void discoverThingFromID(int qrCodeID, final MainActivity mainActivity) {
//
//        try {
//            JSONObject dataJSON = new JSONObject();
//            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
//            dataJSON.put("method", "qrCode");
//            dataJSON.put("data", qrCodeID);
//
//            HTTPRequests.post(dataJSON, Constants.DISCOVER_THING, new HTTPRequests.OKHttpNetwork() {
//                @Override
//                public void onSuccess(String response) {
//
//                    Thing thing = gson.fromJson(response, Thing.class);
//                    mainActivity.showDetailsView(thing);
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

    public static void getThingsProbability(List<ScanResult> scanResults, final DirectionsActivity directionsActivity) {

        WifiData wifiData = new WifiData();

        for (ScanResult sr : scanResults) {
            wifiData.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("wifiData", new JSONObject(gson.toJson(wifiData).toString()));

            Log.d("wifiData", dataJSON.toString());

            HTTPRequests.post(dataJSON, Constants.DISCOVER_WIFI_DATA, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("thingProbability");

                        final ArrayList<ThingProbability> thingProbabilityList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
                            Thing thing = gson.fromJson(jsonArray.getJSONObject(i).getJSONObject("thing").toString(), Thing.class);
                            double probability = jsonArray.getJSONObject(i).getDouble("probability");

                            thingProbabilityList.add(new ThingProbability(thing, probability));
                        }

                        if (thingProbabilityList.get(0).getProbability() > 95) {

                            Thing higherProbabilityThing = thingProbabilityList.get(0).getThing();
                            Thing nextThing = directionsActivity.edgeList.get(0).getOrigin();

                            if (higherProbabilityThing.getID() == nextThing.getID())
                                directionsActivity.updateDirections();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }

    public static void guideToThing(Thing origin, Thing destination, User user, final DirectionsActivity directionsActivity) {

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

                    directionsActivity.edgeList = edgeList;
                    directionsActivity.updateDirections();
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void searchThing(String keyword, final ListView results_listView, final Context context, final ArrayList<Thing> thingList) {

        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("keyword", keyword);

            HTTPRequests.post(dataJSON, Constants.SEARCH_THING, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    Log.d("response searchThing", response);

                    thingList.clear();
                    thingList.addAll((ArrayList<Thing>) gson.fromJson(response, new TypeToken<ArrayList<Thing>>() {
                    }.getType()));

                    fillList(thingList, results_listView, context);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWifiData(List<ScanResult> scanResults, int thingID) {

        WifiData wifiData = scanResultToWifiData(scanResults);
        wifiData.setIDThing(thingID);

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("wifiData", new JSONObject(gson.toJson(wifiData).toString()));

            Log.d("json", dataJSON.toString());

            HTTPRequests.post(dataJSON, Constants.SAVE_WIFI_DATA, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                    Log.d("json", "response: " + response);
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static WifiData scanResultToWifiData(List<ScanResult> scanResult) {

        WifiData wifiDataList = new WifiData();

        for (ScanResult sr : scanResult) {
            wifiDataList.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        return wifiDataList;
    }

    public static void fillList(ArrayList<Thing> thingList, final ListView results_listView, Context context) {

        List<RowData> rowData = new ArrayList<>();
        for (Thing thing : thingList) {
            RowData data = new RowData();
            data.setTitle(thing.getName().getText());
            data.setSubtitle(thing.getLocality().getName().getText());
            rowData.add(data);
        }

        final ListAdapter adapter = new ListAdapter(context, rowData);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                results_listView.setAdapter(adapter);
            }
        });
    }

//    public static void discoverNeighbours(Thing origin, int NEIGHBOUR_DEPTH, final MainActivity mainActivity) {
//        try {
//            JSONObject dataJSON = new JSONObject();
//            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
//            dataJSON.put("thing", new JSONObject(gson.toJson(origin).toString()));
//            dataJSON.put("neighbour_depth", NEIGHBOUR_DEPTH);
//
//            HTTPRequests.post(dataJSON, Constants.DISCOVER_NEIGHBOURS, new HTTPRequests.OKHttpNetwork() {
//                @Override
//                public void onSuccess(String response) {
//
//                    if (response.contains("error")) {
//
//                        ArrayList<Edge> edgeList = gson.fromJson(response, new TypeToken<ArrayList<Edge>>() {
//                        }.getType());
//
//                        Log.d("response showRoute", "" + edgeList.size());
//                        mainActivity.showDirections(edgeList);
//                    }
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
