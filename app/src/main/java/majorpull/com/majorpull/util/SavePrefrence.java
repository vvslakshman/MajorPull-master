package majorpull.com.majorpull.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ResultResponse.Userdata;

/**
 * Created by user102 on 4/2/18.
 */

public class SavePrefrence {
    private static Context context;
    private static final String pref_devicetype = "ANDROID";
    private static final String pref_deviceid = "device_id";
    private static final String userdetail = "user_details";
    private String pref_name;
    private String Key_userdetail = "user_details";

    //    public void getInstance(LoginActivity loginActivity) {
//    }
    private static SavePrefrence instance;

    private SavePrefrence() {
//		...
    }

    public static synchronized SavePrefrence getInstance(Context mcontext) {

        context = mcontext;
        if (instance == null)
            instance = new SavePrefrence();

        return instance;
    }

    //	...
    public void doSomething() {
//		...
    }

    /*deserializing user detail and get it into preference*/
    public Userdata getUserdetail() {
        SharedPreferences sharedPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Userdata mUserdetail = gson.fromJson(sharedPref.getString(Key_userdetail, null), Userdata.class);
        return mUserdetail;
    }

    public void saveUserdetail(Userdata mUserdetail) {
        SharedPreferences sharedPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String muserjson = gson.toJson(mUserdetail);
        editor.putString(Key_userdetail, muserjson);
        editor.commit();
    }

    public void removeUserDeatil(Context context) {
        context.getSharedPreferences(pref_name, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public void saveUserToken(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString("token", token);
        editor.commit();
    }


    public String getDeviceId() {
        SharedPreferences sharepref = context.getSharedPreferences(pref_devicetype, Context.MODE_PRIVATE);
        return sharepref.getString(pref_deviceid, "12345");
    }

    public void savedeviceId(String deviceid) {
        SharedPreferences sharepref = context.getSharedPreferences(pref_deviceid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        editor.putString(pref_deviceid, deviceid);
        editor.commit();
    }


    public String getUserToken() {
        SharedPreferences sharedPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mUserdetail = sharedPref.getString("token", null);
        return mUserdetail;
    }


    //UserInformation
    public static void SetPreferences(Context con, String key, String value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getPreferences(Context con, String key) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        return preferences.getString(key, "");
    }
}
