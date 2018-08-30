package br.com.polieach.urbanassist.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.polieach.urbanassist.model.Locality;
import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.User;
import br.com.polieach.urbanassist.util.Constants;
import br.com.polieach.urbanassist.util.GsonUTCDateAdapter;
import br.com.polieach.urbanassist.util.HTTPRequests;
import br.com.polieach.urbanassist.view.ListAdapter;
import br.com.polieach.urbanassist.view.RowData;

/**
 * Created by Thyag on 20/07/2018.
 */

public class UserController {

    public static void registerUser(User user){

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("user", new JSONObject(gson.toJson(user).toString()));

            HTTPRequests.post(dataJSON, Constants.REGISTER_USER, new HTTPRequests.OKHttpNetwork() {
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

    public static void updateUser(User user){

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("user", new JSONObject(gson.toJson(user).toString()));

            HTTPRequests.post(dataJSON, Constants.UPDATE_USER, new HTTPRequests.OKHttpNetwork() {
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

    public static void rateLocality(Locality locality, User user, int rate){

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("locality",  new JSONObject(gson.toJson(locality).toString()));
            dataJSON.put("user",  new JSONObject(gson.toJson(user).toString()));
            dataJSON.put("rate", rate);

            HTTPRequests.post(dataJSON, Constants.RATE_LOCALITY, new HTTPRequests.OKHttpNetwork() {
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

    public static void rateThing(Thing thing, User user, int rate){

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("thing",  new JSONObject(gson.toJson(thing).toString()));
            dataJSON.put("user",  new JSONObject(gson.toJson(user).toString()));
            dataJSON.put("rate", rate);

            HTTPRequests.post(dataJSON, Constants.RATE_THING, new HTTPRequests.OKHttpNetwork() {
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

    public static void selectRatedLocality(User user, final ListView searchThingList, final Context context){
        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("user",  new JSONObject(gson.toJson(user).toString()));

            HTTPRequests.post(dataJSON, Constants.SELECT_RATED_LOCALITIES, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    ArrayList<Locality> localityList = gson.fromJson(response, new TypeToken<ArrayList<Locality>>(){}.getType());

                    List<RowData> rowData = new ArrayList<>();
                    for(Locality locality : localityList){
                        RowData data = new RowData();
                        data.setTitle(locality.getName().getText());
                        data.setSubtitle(locality.getName().getText());
                        rowData.add(data);
                    }

                    final ListAdapter adapter = new ListAdapter(context, rowData);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            searchThingList.setAdapter(adapter);
                        }
                    });
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void selectRatedThings(User user, final ListView searchThingList, final Context context){
        try {
            JSONObject dataJSON = new JSONObject();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("user",  new JSONObject(gson.toJson(user).toString()));

            HTTPRequests.post(dataJSON, Constants.SELECT_RATED_THINGS, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    ArrayList<Thing> thingList = gson.fromJson(response, new TypeToken<ArrayList<Thing>>(){}.getType());

                    List<RowData> rowData = new ArrayList<>();
                    for(Thing thing : thingList){
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
                            searchThingList.setAdapter(adapter);
                        }
                    });
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
