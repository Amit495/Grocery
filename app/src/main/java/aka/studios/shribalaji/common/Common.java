package aka.studios.shribalaji.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import aka.studios.shribalaji.retrofit.ClientAPI;
import aka.studios.shribalaji.retrofit.RetrofitClient;

public class Common {
    public static final String LOGGED_IN = "loggedIn";
    public static final String USER_ID = "user_id";

    // [Local Server]
   public static final String BASE_URL ="http://127.0.0.1/";
   private static final String API_BASE_URL ="http://127.0.0.1/api/";

    public static ClientAPI getAPI() {
        return RetrofitClient.getClient(API_BASE_URL).create(ClientAPI.class);
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i=0;i<info.length;i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
