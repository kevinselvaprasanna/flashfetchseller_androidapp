package in.flashfetch.sellerapp.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by KRANTHI on 05-06-2016.
 */
public class Utils {

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean isValidEmail(String emailText) {
        if(emailText == null){
            return false;
        }else{
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(emailText).matches();
        }
    }

    public static boolean checkSamePassword(String newPassword, String confirmPassword){
        if(newPassword.equals(confirmPassword)){
            return true;
        }
        return false;
    }

    public static int[] generatePrimeNumbers(int noOfPrimeNumbers){
        int a = noOfPrimeNumbers;
        int x = 1;
        int currentNumber = 0;
        boolean isPrimeNumber = false;
        int[] list = new int[a];
        list[0] = 2;
        for(int i=1 ; i<100 ; i++){
            for (int j = i-1 ; j>1 ; j--){
                if(i%j != 0){
                    isPrimeNumber = true;
                    currentNumber = i;
                }else {
                    isPrimeNumber = false;
                }
            }
            if(isPrimeNumber){
                if(x<a){
                    list[x] = currentNumber;
                    x++;
                }
            }
        }
        return list;
    }

    public static boolean checkPlayServices(Activity activity) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(activity,resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tag", "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static boolean isInternetAvailable(Context ctx) {
        boolean lRetVal = false;

        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                if (null != nInfo) {
                    lRetVal = nInfo.isConnectedOrConnecting();
                }
            }
        } catch (Exception e) {
            return lRetVal;
        }

        return lRetVal;
    }

    public static boolean isSelectedDateGreater(Date startDate, Date endDate){
        if (endDate.after(startDate)) {
            return true;
        }
        return false;
    }
}
