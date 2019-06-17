package majorpull.com.majorpull.CommonMethod;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by user102 on 3/6/18.
 */

public class ToastShow {


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
