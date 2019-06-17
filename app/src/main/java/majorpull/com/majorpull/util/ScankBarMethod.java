package majorpull.com.majorpull.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by user102 on 4/2/18.
 */

public class ScankBarMethod {

    public static void showSnack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
