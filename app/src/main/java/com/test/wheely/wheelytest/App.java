package com.test.wheely.wheelytest;

import android.app.Application;
import android.content.Context;

import com.test.wheely.wheelytest.GetDataService;

/**
 * Created by Александр on 06.08.2014.
 */
public class App extends Application {

    public static Context mContext;

    public static String username;
    public static String password;

    public static boolean close_internet=false;


    @Override
    public void onCreate() {
        mContext=this;
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        GetDataService.mConnection.disconnect();
        super.onLowMemory();
    }
}
