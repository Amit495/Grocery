package aka.studios.shribalaji.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static int TYPE_NOT_CONNECTED = 0;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;

    public static int getConnectivityStatus(Context context)
    {
        String status = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                return TYPE_WIFI;
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context)
    {
        int connection = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (connection == NetworkUtil.TYPE_WIFI)
        {
            status = "WIFI";
        }
        else if (connection == NetworkUtil.TYPE_MOBILE)
        {
            status = "Mobile Data";
        }
        else if (connection == TYPE_NOT_CONNECTED)
        {
            status = "No Internet Connection";
        }
        return status;
    }
}
