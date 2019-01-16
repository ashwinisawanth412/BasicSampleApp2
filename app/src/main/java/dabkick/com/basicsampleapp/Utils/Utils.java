package dabkick.com.basicsampleapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKB(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    static public boolean isWifiConnected(Activity mActivity) {
        if (mActivity == null)
            return false;

        ConnectivityManager connMgr = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        return isWifiConn;

    }

    public static Date millisToDate(long milliSeconds) {
        return new Date(milliSeconds);
    }

    public static String dateToString(long milliSec) {
        DateFormat format = new SimpleDateFormat("E, dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        format.setTimeZone(tz);
        return format.format(millisToDate(milliSec));
    }

    public static String millisToTime(long milliSeconds) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date date = new Date(milliSeconds);
        String pattern = "hh:mm aa";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(tz);
        return dateFormat.format(date);
    }
}


