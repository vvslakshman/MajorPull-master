package majorpull.com.majorpull.user.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityConfirmRidingBinding;

/**
 * Created by user102 on 3/20/18.
 */

public class ConfirmRidingActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityConfirmRidingBinding confirmRidingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confirmRidingBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_riding);
        confirmRidingBinding.toolbar.ivMenu.setVisibility(View.GONE);
//        confirmRidingBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        confirmRidingBinding.toolbar.ivPing.setVisibility(View.GONE);
        confirmRidingBinding.toolbar.tvOk.setVisibility(View.VISIBLE);
        confirmRidingBinding.toolbar.tvOk.setOnClickListener(this);
        confirmRidingBinding.toolbar.ivBack.setOnClickListener(this);

        confirmRidingBinding.ivConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_confirm:
//                submitDialog();

                break;

            case R.id.iv_back:
                onBackPressed();


                break;

            case R.id.tv_ok:
//                dialog_MoveUp();

                Intent intent = new Intent(ConfirmRidingActivity.this, MainActivity.class);
                intent.putExtra("start_ride_confirm_dialog", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
    }

    private void dialog_MoveUp() {

        final Dialog moveup = new Dialog(this, R.style.customStyle);
        moveup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        moveup.setContentView(R.layout.dailog_thanks_booking_layout);
        Window window = moveup.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        moveup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        moveup.show();


        moveup.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveup.dismiss();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888) {
            Intent i = new Intent();
            i.putExtra("type", "yes");
            this.setResult(888, i);
            this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
