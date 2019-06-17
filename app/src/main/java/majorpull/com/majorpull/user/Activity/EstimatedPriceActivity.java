package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityEstimatedPriceLayoutBinding;


/**
 * Created by user102 on 3/20/18.
 */

public class EstimatedPriceActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityEstimatedPriceLayoutBinding productDetialNextBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productDetialNextBinding = DataBindingUtil.setContentView(this, R.layout.activity_estimated_price_layout);
        productDetialNextBinding.tvBookmyride.setOnClickListener(this);
        productDetialNextBinding.toolbar.ivMenu.setVisibility(View.GONE);
        productDetialNextBinding.toolbar.ivPing.setVisibility(View.GONE);
        productDetialNextBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        productDetialNextBinding.toolbar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_bookmyride:
                startActivity(new Intent(getApplicationContext(), ConfirmRidingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case R.id.iv_back:
                onBackPressed();
//                Intent i = new Intent();
//                i.putExtra("type", "no");
//                setResult(888, i);
//                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888) {
            Intent i = new Intent();
            i.putExtra("type", "yes");
            this.setResult(888, i);
            this.finish();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent();
//        i.putExtra("type", "no");
//        this.setResult(888, i);
//        this.finish();
//    }
}
