package aka.studios.shribalaji.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import aka.studios.shribalaji.utils.NetworkUtil;

public class NetworkReceiver extends BroadcastReceiver {
    static String status = null;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        status = NetworkUtil.getConnectivityStatusString(context);
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        if(status.equals("WIFI")){
            //your code when wifi enable
        }
        else if(status.equals("Mobile Data")){
            //your code when TYPE_MOBILE network enable
        }
        else if(status.equals("No Internet Connection")){
            //your code when no network connected
//            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        }
    }
}
