package dabkick.com.basicsampleapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {

    static public void setUserName(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("username", str);
        editor.commit();
    }

    static public String getUserName(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    static public void setUserProfileImg(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("userProfileImg", str);
        editor.commit();
    }

    static public String getUserProfileImg(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("userProfileImg", "");
    }


    public static void clearAll(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }
}
