package com.test.wheely.wheelytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Александр on 06.08.2014.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        if(NetworkUtil.isNetworkAvailable()){
         Intent i=new Intent(App.mContext,GetDataService.class);
            if(!App.close_internet) App.mContext.startService(i);
        }
       // Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}

