package ajaysrinivas.putmeintouch;

import android.app.Application;

/**
 * Created by AJ on 4/8/2017.
 */

public class ApplicationConnectivity extends Application {

    private static ApplicationConnectivity mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ApplicationConnectivity getmInstance(){
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
