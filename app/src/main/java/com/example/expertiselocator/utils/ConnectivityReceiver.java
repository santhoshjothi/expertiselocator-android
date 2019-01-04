package com.example.expertiselocator.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by PPMS on 01-11-2017.
 */
public class ConnectivityReceiver extends BroadcastReceiver {


    public static ConnectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {


        //action need to be done when mobile is switched on
        try {
            if (isConnected()) {
                Log.v("Checking Networking ", "complted");
                //service.setAction(RECEIVE_BOOT_COMPLETED);
//                Intent syncServiceIntent = new Intent(context, SynService.class);
//                context.startService(syncServiceIntent);


                /***** For start Service  ****/
//                Intent myIntent = new Intent(context, SynService.class);
//                context.startService(myIntent);

            }


        } catch (Exception e) {
            Log.e("ServiceReceiverBOOT", e.toString());
        }


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) NetworkUtils.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
