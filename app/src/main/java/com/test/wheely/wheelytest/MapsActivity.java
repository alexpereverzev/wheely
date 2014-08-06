package com.test.wheely.wheelytest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import autobahn.WebSocketConnection;
import autobahn.WebSocketConnectionHandler;
import autobahn.WebSocketException;

public class MapsActivity extends ActionBarActivity {

    private PendingIntent pIntent;

    private GoogleMap googleMap;

    private AlarmManager alarm;

    private Button disconnect;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        double [] lon=intent.getDoubleArrayExtra("lon");
            double [] lat= intent.getDoubleArrayExtra("lat");

            for (int i=0; i<lon.length; i++){
                //MarkerOptions marker =new MarkerOptions().position(new LatLng(lat[i], lon[i]));
                if(googleMap!=null) setUpMap(lat[i], lon[i]);
                  //  googleMap.addMarker(marker);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent data=getIntent();
        setTitle("Map");
      final  Intent i=new Intent(this,GetDataService.class);

        disconnect=(Button) findViewById(R.id.discon);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   alarm.cancel(pIntent);
                stopService(i);
                GetDataService.mConnection.disconnect();
                App.close_internet=true;
            }
        });
      //  setUpMapIfNeeded();
        i.putExtra("username",data.getStringExtra("username"));
        i.putExtra("password",data.getStringExtra("password"));
      PendingIntent  pendingIntent = PendingIntent.getService(this, 0, i, 0);
        startService(i);
        registerReceiver(myReceiver, new IntentFilter("com.test.wheely"));
       // scheduleAlarm();
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
   //     setUpMapIfNeeded();
        initilizeMap();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call  once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(double lat, double lon) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
    }


    private final WebSocketConnection mConnection = new WebSocketConnection();

   // private final WAP mConnection = new WebSocketConnection();


    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReciver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        pIntent = PendingIntent.getBroadcast(this, MyAlarmReciver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 5000; // 5 seconds
        alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);

    }

    private void initilizeMap() {
        if (googleMap == null) {
            FragmentManager fmanager = getSupportFragmentManager();
            Fragment fragment = fmanager.findFragmentById(R.id.map);
            SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
            googleMap = supportmapfragment.getMap();

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }


    public void setUpgradeButton(int title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customBar = View.inflate(getApplicationContext(), R.layout.custom_actionbar, null);
        actionBar.setCustomView(customBar);
        TextView textTitle = (TextView) findViewById(R.id.txtTitle);
        textTitle.setText(title);


    }



}
