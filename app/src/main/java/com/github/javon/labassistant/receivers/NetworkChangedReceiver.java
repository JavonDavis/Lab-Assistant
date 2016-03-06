package com.github.javon.labassistant.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.github.javon.labassistant.events.network.NetworkConnectedEvent;
import com.github.javon.labassistant.events.network.NetworkOfflineEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by shane on 2/8/16.
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private EventBus bus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                mobile != null && mobile.isConnectedOrConnecting();

        if (isConnected) {
            bus.post(new NetworkConnectedEvent());
            Log.d("Network Available ", "YES");
        } else {
            bus.post(new NetworkOfflineEvent());
            Log.d("Network Available ", "NO");
        }


    }
}
