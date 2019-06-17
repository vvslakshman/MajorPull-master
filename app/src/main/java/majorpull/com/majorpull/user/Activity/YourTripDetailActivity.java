package majorpull.com.majorpull.user.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityYourTripDetialUserBinding;
import majorpull.com.majorpull.util.CommonMethod;

/**
 * Created by user102 on 3/22/18.
 */

public class YourTripDetailActivity extends AppCompatActivity {


    private ActivityYourTripDetialUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_your_trip_detial_user);

        if (CommonMethod.getPrefrence(YourTripDetailActivity.this, "Majorpull_user").equals("Your_Trip")) {
            binding.llFooter.setVisibility(View.VISIBLE);
            Log.v("Majorpull_user", "Your_Trips");
        } else if ((CommonMethod.getPrefrence(YourTripDetailActivity.this, "Majorpull_user").equals("Scheduled_Trip"))) {

            binding.llFooter.setVisibility(View.GONE);
            Log.v("Majorpull_user", "Scheduled_Trip");

        }
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                CommonMethod.clearPrefrence(YourTripDetailActivity.this);
                Log.v("Majorpull_user", "ClearPrefrence");

            }
        });


    }
}
