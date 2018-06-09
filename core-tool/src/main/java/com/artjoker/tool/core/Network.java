package com.artjoker.tool.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Network {

    private Network() {

    }

    public static Network getInstance() {
        return NetworkHolder.INSTANCE;
    }

    public boolean isConnected(final Context context) {
        return checkConnection(((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo());
    }

    private boolean checkConnection(final NetworkInfo networkInfo) {
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void checkConnectionMessage(final Context context) {
        Notification.getInstance().show(context, R.string.error_check_connection);
    }

    private static class NetworkHolder {
        private static final Network INSTANCE = new Network();
    }

}