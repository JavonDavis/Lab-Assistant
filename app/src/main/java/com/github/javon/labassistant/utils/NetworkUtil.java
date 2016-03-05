package com.github.javon.labassistant.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by shane on 2/9/16.
 */
public class NetworkUtil {

    private NetworkUtil() {
        throw new AssertionError("Cannot instantiate this class");
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return null != connectivityManager.getActiveNetworkInfo() && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
