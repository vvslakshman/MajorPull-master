package majorpull.com.majorpull.util;

import android.util.Log;

/**
 * Created by user102 on 3/5/18.
 */

public class DebugLog {

    public static void log(int i, String retrofit, String s) {
        Log.v(retrofit, s.toString());
    }

}
