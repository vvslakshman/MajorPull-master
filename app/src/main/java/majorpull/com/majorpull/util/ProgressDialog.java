package majorpull.com.majorpull.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import majorpull.com.majorpull.R;

/**
 * Created by user102 on 4/2/18.
 */

public class ProgressDialog {

    public static Dialog progressDio;

    public static void showProgressDio(Context context) {
        progressDio = new Dialog(context);
        progressDio.setContentView(LayoutInflater.from(context).inflate(R.layout.progress_diolog, null));
        progressDio.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDio.setCancelable(false);
        progressDio.show();
    }

    public static void hideProgressDio() {
        if (progressDio != null) {
            if (progressDio.isShowing())
                progressDio.dismiss();
            progressDio = null;
        }
    }


}
