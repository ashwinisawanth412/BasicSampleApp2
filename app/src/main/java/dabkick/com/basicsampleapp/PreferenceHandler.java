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

    static public void setUserId(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("userId", str);
        editor.commit();
    }

    static public String getUserId(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("userId", "");
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

    static public void setDevId(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("devId", str);
        editor.commit();
    }

    static public String getDevId(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("devId", "");
    }
    static public void setDevKey(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("devKey", str);
        editor.commit();
    }

    static public String getDevKey(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("devKey", "");
    }

    static public void setUserId(Context context, String str) {
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE).edit();
        editor.putString("userId", str);
        editor.apply();
    }

    static public String getUserId(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        return preferences.getString("userId", "");
    }

    public static void clearAll(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SampleAppPref", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

}
