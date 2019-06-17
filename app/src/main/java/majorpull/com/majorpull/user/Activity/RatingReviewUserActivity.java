package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityRatingReviewUserBinding;

/**
 * Created by user102 on 3/23/18.
 */

public class RatingReviewUserActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityRatingReviewUserBinding reviewUserBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviewUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_rating_review_user);
        reviewUserBinding.toolbar.ivMenu.setVisibility(View.GONE);
        reviewUserBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        reviewUserBinding.toolbar.ivBack.setOnClickListener(this);
        reviewUserBinding.toolbar.ivPing.setVisibility(View.GONE);
        reviewUserBinding.toolbar.ivBack.setOnClickListener(this);
        reviewUserBinding.tvSubmit.setOnClickListener(this);
        reviewUserBinding.ivProfile.setImageResource(R.drawable.user_immg);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:

                Intent i2 = new Intent();
                i2.putExtra("type", "no");
                setResult(888, i2);
                finish();


                break;

            case R.id.tv_submit:

                Intent i = new Intent();
                i.putExtra("type", "yes");
                setResult(888, i);
                finish();
//                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
//                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("type", "no");
        setResult(888, i);
        finish();

    }
}
