package majorpull.com.majorpull.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user102 on 3/27/18.
 */

public class CommonMethod {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;


    public static void setPrefrence(Context context, String key, String value) {
        preferences = context.getSharedPreferences("MajorPull", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getPrefrence(Context context, String key) {
        String value = "";
        preferences = context.getSharedPreferences("MajorPull", Context.MODE_PRIVATE);
        value = preferences.getString(key, "");
        return value;
    }

    public static void clearPrefrence(Context context) {
        preferences = context.getSharedPreferences("MajorPull", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }




}
