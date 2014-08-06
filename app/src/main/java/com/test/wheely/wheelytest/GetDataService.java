package com.test.wheely.wheelytest;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import autobahn.WebSocketConnection;
import autobahn.WebSocketConnectionHandler;
import autobahn.WebSocketException;

/**
 * Created by Александр on 05.08.2014.
 */
public class GetDataService extends Service{

    private double lat;
    private double lot;

    private static String username;
    private static String password;

    private double [] lon_massive;
    private double [] lat_massive;

    private static final long UPDATE_INTERVAL = 1 * 5 * 1000;
    private static final long DELAY_INTERVAL = 0;

    private Timer timer=new Timer();

    private final Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Runnable refresher = new Runnable() {
        public void run() {
      start();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username=intent.getStringExtra("username");
        password=intent.getStringExtra("password");

//        handler.postDelayed(refresher,5000);
        start();
      //  super.onStartCommand(intent, flags, startId);

        return 0;
    }


 //       return START_STICKY;
   // }

    @Override
    public void onDestroy() {
        timer.cancel();

        super.onDestroy();
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here

            lat=location.getLatitude();
            lot=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };





    public static final WebSocketConnection mConnection = new WebSocketConnection();


    private void start() {

     /*LocationManager   mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
                10, mLocationListener);
*/
        GPSTracker gpsTracker = new GPSTracker(this);
        Location location=gpsTracker.getLocation();
        if(location!=null){
            lat= gpsTracker.getLocation().getLatitude();
            lot= gpsTracker.getLocation().getLongitude();

        }
        else{
            lat=53.02;
            lot=37.02;
        }


        final String wsuri = "ws://mini-mdt.wheely.com?username="+App.username+"&password="+App.password;


        try {
            mConnection.connect(wsuri, new WebSocketConnectionHandler() {

                @Override
                public void onTextMessage(JSONArray payload) {
                    if (payload!=null){
                     boolean b;
                    }
                    super.onTextMessage(payload);

                }

                @Override
                public void onOpen() {
                    // Log.d(TAG, "Status: Connected to " + wsuri);
                    JSONObject json=new JSONObject();
                    try {
                        json.put("lat", lat);
                        json.put("lon", lat);

                        String sjson=json.toString();
                        mConnection.sendTextMessage(json.toString());
                        boolean b=mConnection.isConnected();
                        System.out.print("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //  mConnection.sendTextMessage("Hello, world!");

                }



                @Override
                public void onTextMessage(String payload) {
                    //Log.d(TAG, "Got echo: " + payload);
                    try {
                        if(!payload.isEmpty()){
                            JSONArray array=new JSONArray(payload);
                         lon_massive=new double[array.length()];
                            lat_massive=new double[array.length()];

                            for(int i=0; i<array.length(); i++)
                            {
                             JSONObject obj=array.getJSONObject(i);
                          lon_massive[i]=   obj.getDouble("lon");
                          lat_massive[i]=   obj.getDouble("lat");

                            }

                        }
                        Intent i = new Intent("com.test.wheely");
                        i.putExtra("lon",lon_massive);
                        i.putExtra("lat",lat_massive);

                        sendBroadcast(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onClose(int code, String reason) {
                    //Log.d(TAG, "Connection lost.");

                }
            });
        } catch (WebSocketException e) {

            //Log.d(TAG, e.toString());
        }
    }



}
