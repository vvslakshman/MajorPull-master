package majorpull.com.majorpull.user.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FragmentDriverRequestLayoutBinding;
import majorpull.com.majorpull.user.Activity.MakePaymentActivity;
import majorpull.com.majorpull.user.Activity.RatingReviewUserActivity;

/**
 * Created by user102 on 3/20/18.
 */

public class DriverRequestFragment extends Fragment implements View.OnClickListener {


    private View view;
    private Context context;
    private FragmentDriverRequestLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_driver_request_layout, container, false);
        context = container.getContext();
        view = binding.getRoot();

        binding.tvCancelRide.setOnClickListener(this);
        binding.tvCalldriver.setOnClickListener(this);
        binding.tvRating.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_cancel_ride:

                break;
            case R.id.tv_calldriver:

                callDriverDialog();

                break;
            case R.id.tv_rating:
                startActivity(new Intent(context, RatingReviewUserActivity.class));
                break;

        }

    }

    private void callDriverDialog() {

        final Dialog dailog = new Dialog(getActivity());
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        dailog.setContentView(R.layout.dailog_thanks_booking_layout);
        dailog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submitDialog();
                dailog.dismiss();

            }
        });


    }

    private void submitDialog() {


        Dialog dailog = new Dialog(getActivity());
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        dailog.setContentView(R.layout.dailog_payment_sucessfull);
        dailog.show();
        dailog.findViewById(R.id.tv_ratingreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MakePaymentActivity.class));
            }
        });


    }


}
