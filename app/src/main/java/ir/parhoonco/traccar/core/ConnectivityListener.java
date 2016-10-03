package ir.parhoonco.traccar.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mao on 9/19/2016.
 */
public class ConnectivityListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationLoader.isInternetAvailable();
    }
}
