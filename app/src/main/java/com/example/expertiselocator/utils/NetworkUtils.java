package com.example.expertiselocator.utils;

import android.app.Application;


/**
 * Created by PPMS on 19-01-2018.
 */

public class NetworkUtils extends Application {

    private static NetworkUtils mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized NetworkUtils getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
