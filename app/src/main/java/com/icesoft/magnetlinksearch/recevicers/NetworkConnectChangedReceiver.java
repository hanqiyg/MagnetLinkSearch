package com.icesoft.magnetlinksearch.recevicers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.icesoft.magnetlinksearch.events.NetworkChangeEvent;
import com.icesoft.magnetlinksearch.utils.NetworkUtils;
import org.greenrobot.eventbus.EventBus;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkChangedReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
            /*判断当前网络时候可用以及网络类型*/
            boolean isConnected = NetworkUtils.isNetworkConnected(context);
            int networkType = NetworkUtils.getConnectedType(context);
            EventBus.getDefault().post(new NetworkChangeEvent(isConnected, networkType));
        }
    }
}
