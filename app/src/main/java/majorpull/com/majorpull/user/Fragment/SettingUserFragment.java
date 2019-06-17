package majorpull.com.majorpull.user.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.LogoutResponse;
import ResultResponse.Userdata;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FramgmentUserSettingLayoutBinding;
import majorpull.com.majorpull.user.Activity.ChangePasswordUserActivity;
import majorpull.com.majorpull.user.Activity.MainActivity;
import majorpull.com.majorpull.user.Activity.NotificationUserActivity;
import majorpull.com.majorpull.user.Activity.UserLoginActivity;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.FinalKeywordClass;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user102 on 3/22/18.
 */

public class SettingUserFragment extends Fragment implements View.OnClickListener {


    private Context mcontext;
    private View view;
    private FramgmentUserSettingLayoutBinding mbinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mbinding = DataBindingUtil.inflate(inflater, R.layout.framgment_user_setting_layout, container, false);


        mcontext = container.getContext();
        view = mbinding.getRoot();


        mbinding.ivToggleSwitch.setOnClickListener(this);
        mbinding.rlChangepassword.setOnClickListener(this);
        mbinding.rlLogout.setOnClickListener(this);


        return view;
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        settingBinding = DataBindingUtil.setContentView(this, R.layout.framgment_user_setting_layout);
//        settingBinding.toolbar.ivMenu.setVisibility(View.GONE);
//        settingBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
//        settingBinding.toolbar.ivBack.setOnClickListener(this);
//        settingBinding.ivToggleSwitch.setOnClickListener(this);
//        settingBinding.rlNotification.setOnClickListener(this);
//        settingBinding.rlChangepassword.setOnClickListener(this);
//        settingBinding.rlLogout.setOnClickListener(this);
//        settingBinding.toolbar.ivPing.setOnClickListener(this);
//
//
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_toggle_switch:
                toggleSwitchMethod(v);
                break;

            case R.id.rl_changepassword:
                startActivity(new Intent(mcontext, ChangePasswordUserActivity.class));

                break;

            case R.id.rl_logout:
                ExitDialog();
                break;
        }

    }


    //    LogoutDialogMethod
    private void ExitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("MajorPull");
        builder.setMessage("Are you sure want to Log out?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int paramInt) {
//                CommonMethod.clearPrefrence(mcontext);

                if (IsOnLineMethod.isOnline(getActivity())) {
                    call_logoutApi();
                } else {
                    ScankBarMethod.showSnack(mbinding.llParent, getResources().getString(R.string.check_internet_connection));
                }
                dialog.dismiss();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int paramInt) {

                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void call_logoutApi() {


        ProgressDialog.showProgressDio(getActivity());
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(getActivity()).getUserToken()).
                create(ApiInterface.class);
        final Map maplogout = new HashMap();

        maplogout.put(ApiClass.getmApiClass().USER_ID, SavePrefrence.getInstance(mcontext).getUserdetail().getUserId());
        maplogout.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        maplogout.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);
        Call<LogoutResponse> call = mApiInterface.logout(maplogout);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {


                LogoutResponse logoutResponse = response.body();
                if (response.isSuccessful()) {
                    ProgressDialog.hideProgressDio();

                    SavePrefrence.getInstance(getActivity()).removeUserDeatil(getActivity());
                    startActivity(new Intent(getActivity(), UserLoginActivity.class));
                    getActivity().finish();

                    Log.v("Response:", response.body() + "");

                } else {

                    ScankBarMethod.showSnack(mbinding.llParent, "Somthing Wronge" + response.toString());
                }

            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                ProgressDialog.hideProgressDio();

                Log.v("OnFailureMethod:", t + "");
            }
        });


    }

    //        ToggleSwicthMethod
    private void toggleSwitchMethod(View view) {
        if (mbinding.ivToggleSwitch.isSelected()) {
            mbinding.ivToggleSwitch.setImageResource(R.drawable.toggle_on);
            view.setSelected(false);
        } else {
            mbinding.ivToggleSwitch.setImageResource(R.drawable.toggle_off);
            view.setSelected(true);
        }
    }
}
