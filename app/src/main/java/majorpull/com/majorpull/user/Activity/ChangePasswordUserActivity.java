package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.ChangePasswordResponse;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityChangepasswordUserBinding;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user102 on 3/23/18.
 */

public class ChangePasswordUserActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityChangepasswordUserBinding changepasswordUserBinding;
    private String currenct_password, new_password, confirm_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changepasswordUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_changepassword_user);
        changepasswordUserBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        changepasswordUserBinding.toolbar.ivMenu.setVisibility(View.GONE);
        changepasswordUserBinding.toolbar.ivPing.setOnClickListener(this);
        changepasswordUserBinding.toolbar.ivBack.setOnClickListener(this);
        changepasswordUserBinding.tvSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_ping:
                startActivity(new Intent(this, NotificationUserActivity.class));
                break;
            case R.id.tv_submit:


                validationChangePassword();

                break;
        }

    }

    private void validationChangePassword() {

        currenct_password = changepasswordUserBinding.etCurrentpassword.getText().toString();
        if (changepasswordUserBinding.etCurrentpassword.length() < 1) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_current_password));
//            changepasswordUserBinding.etCurrentpassword.setError(getResources().getString(R.string.enter_current_password));
        } else if (changepasswordUserBinding.etCurrentpassword.length() < 6) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.pass_mini_length));
//            changepasswordUserBinding.etCurrentpassword.setError(getResources().getString(R.string.pass_mini_length));
        }
//        else if () {
//
//            get password else current password not match
//
//        }

        //New Password
        else if (changepasswordUserBinding.etNewpassword.length() < 1) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_new_password));
//            changepasswordUserBinding.etNewpassword.setError(getResources().getString(R.string.enter_new_password));
        } else if (changepasswordUserBinding.etNewpassword.length() < 6) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.pass_mini_length));
//            changepasswordUserBinding.etNewpassword.setError(getResources().getString(R.string.pass_mini_length));
        }
        //Cofirm Password
        else if (changepasswordUserBinding.etConfirmpassword.length() < 1) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_new_password));
//            changepasswordUserBinding.etConfirmpassword.setError(getResources().getString(R.string.enter_password));
        } else if (changepasswordUserBinding.etConfirmpassword.length() < 6) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.pass_mini_length));
//            changepasswordUserBinding.etConfirmpassword.setError(getResources().getString(R.string.pass_mini_length));
        } else if (!changepasswordUserBinding.etNewpassword.getText().toString().trim().equalsIgnoreCase(changepasswordUserBinding.etConfirmpassword.getText().toString().trim())) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.password_not_match));
        } else if (IsOnLineMethod.isOnline(ChangePasswordUserActivity.this)) {

//            ChangePassword Call Api
            callChangePasswordApi();
        }
//        All field Are Verify
        else {

            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.check_internet_connection));
        }


    }

    private void callChangePasswordApi() {


        ProgressDialog.showProgressDio(ChangePasswordUserActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(ChangePasswordUserActivity.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapchangePassword = new HashMap();
        mapchangePassword.put(ApiClass.getmApiClass().USER_ID, SavePrefrence.getInstance(ChangePasswordUserActivity.this).getUserdetail().getUserId());

        mapchangePassword.put(ApiClass.getmApiClass().CURRENT_PASSWORD, changepasswordUserBinding.etCurrentpassword.getText().toString());
        mapchangePassword.put(ApiClass.getmApiClass().NEW_PASSWORD, changepasswordUserBinding.etNewpassword.getText().toString());


        Call<ChangePasswordResponse> call = mApiInterface.changepassword(mapchangePassword);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {

                if (response.isSuccessful()) {

                    ChangePasswordResponse changePasswordResponse = response.body();


                    ProgressDialog.hideProgressDio();
                } else {

                    Log.v("Result :", "Somthing_Wronge");

                }

            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {

                ProgressDialog.hideProgressDio();
                Log.v("OnFailure", t + "");


            }
        });


    }
}
