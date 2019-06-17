package majorpull.com.majorpull.user.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.CheckUserResponse;
import ResultResponse.ForgotResponse;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityForgotPasswordBinding;
import majorpull.com.majorpull.databinding.FragmentUserprofileLayoutBinding;
import majorpull.com.majorpull.util.ContectivityStatus;
import majorpull.com.majorpull.util.Country_CodeListActivity;
import majorpull.com.majorpull.util.DebugLog;
import majorpull.com.majorpull.util.FinalKeywordClass;
import majorpull.com.majorpull.util.HideKeyboardMethod;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user102 on 3/19/18.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityForgotPasswordBinding forgotBinding;
    private String countrycode = "", country = "", countryId = "", plush = "+", verifypass;

    private String firstname, lastname, email, password, social_id, social_type, mobilenumber,
            otpNumber = "", newpassword = "", cofirmpassword;
    private com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;
    private String TAG = "1234";

    private String mVerificationId = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private String verificationId = "", code = "";
    private String update_key, comingfrom = "";
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forgotBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        forgotBinding.tvSubmit.setOnClickListener(this);

        forgotBinding.etCountrycode.setOnClickListener(this);


    }

    private void validationforgo_mobile() {


        if (forgotBinding.etMobilenumber.getText().toString().trim().equalsIgnoreCase("")) {

//            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_email));
            forgotBinding.etMobilenumber.setError(getResources().getString(R.string.mobilevalidation));
        } else if (forgotBinding.etMobilenumber.length() < 7) {
            forgotBinding.etMobilenumber.setError(getResources().getString(R.string.mobileno_mini_length_error));
        } else if (forgotBinding.etMobilenumber.length() > 15) {
            forgotBinding.etMobilenumber.setError(getResources().getString(R.string.mobileno_maxlength_error));
        } else if (ContectivityStatus.getConnectivityStatus(this)) {
            HideKeyboardMethod.hideKeyboard(this);

            check_userApi();
//            serverRequestForForgotPassword();
        } else {

            ScankBarMethod.showSnack(forgotBinding.llParent, getResources().getString(R.string.check_internet_connection));
        }

    }

    private void check_userApi() {
        ProgressDialog.showProgressDio(ForgotPasswordActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(ForgotPasswordActivity.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapcheckuser = new HashMap();
        mapcheckuser.put(ApiClass.getmApiClass().PHONE, forgotBinding.etMobilenumber.getText().toString());


        Call<CheckUserResponse> call = mApiInterface.chekc_user(mapcheckuser);
        call.enqueue(new Callback<CheckUserResponse>() {
            @Override
            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {

                ProgressDialog.hideProgressDio();

                if (response.isSuccessful()) {


                    CheckUserResponse mcheckuserresponse = response.body();
                    if (mcheckuserresponse != null && mcheckuserresponse.getStatus() == true) {


                        firebaseOtpRequest();
                        forgotverification_Dailog();
//                        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                    } else {
                        Log.v("Response:", "" + mcheckuserresponse.getMessage().toString());
                        DebugLog.log(1, FinalKeywordClass.RESPONSE, "Something_Wrong");
                        DebugLog.log(1, "Error", "" + response.message().toString());
//                        ScankBarMethod.showSnack(getCurrentFocus(), response.message().toString());
                        ProgressDialog.hideProgressDio();


                    }

                } else {
                    try {
                        System.out.println(response.errorBody().string().toString());
                        //show error about response
                        //somthing went wrong
                        DebugLog.log(1, FinalKeywordClass.RESPONSE, response.errorBody().string().toString() + "");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                t.printStackTrace();
                ProgressDialog.hideProgressDio();
                DebugLog.log(1, "OnFailure", t + "");

            }
        });


    }

    private void firebaseOtpRequest() {
        firebaseOTP(true);
    }

    private void firebaseOTP(boolean b) {

        PhoneAuthProvider phone_auth = PhoneAuthProvider.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, FinalKeywordClass.ONVerificationComplite + phoneAuthCredential);

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    ProgressDialog.hideProgressDio();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    ProgressDialog.hideProgressDio();
                }

                // Show a message and update the UI

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, FinalKeywordClass.OnCodeSent + verificationId.toString());
                ProgressDialog.hideProgressDio();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


        // if (isFirstTime) {
        phone_auth.verifyPhoneNumber("+" + countrycode + forgotBinding.etMobilenumber.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        DebugLog.log(1, "CountryCode:", countrycode + forgotBinding.etMobilenumber.getText().toString());

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FinalKeywordClass.PhoneVerify, FinalKeywordClass.Signincreditional_success);
                            DebugLog.log(1, FinalKeywordClass.PhoneVerify, FinalKeywordClass.Signincreditional_success);

                            // FirebaseUser user = task.getResult().getUser();
                            FirebaseAuth.getInstance().signOut();

                            //signup api

                            //response userdetail ko save kerna
                            //otpverificaton and signup login ko close kerna
                            // app landing page
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, FinalKeywordClass.SignInWithCredential_Failure, task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }

                    }
                });


//
//        //To sign out a user, call signOut:
//        FirebaseAuth.getInstance().signOut();


    }

    private void forgotverification_Dailog() {


        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        dialog.setContentView(R.layout.dailog_forgot_verify_activity);
        final EditText et_otpNumber = dialog.findViewById(R.id.et_otpnumber);
        final EditText et_newpassword = dialog.findViewById(R.id.et_newpassword);
        final EditText et_confirmpassword = dialog.findViewById(R.id.etconfirmpassword);
        dialog.show();
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               validate
//               dsfd
                otpNumber = et_otpNumber.getText().toString();
                newpassword = et_newpassword.getText().toString();
                cofirmpassword = et_confirmpassword.getText().toString();
                dialog.dismiss();

                forgotValidation();

            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void forgotValidation() {

        if (otpNumber.length() != 0) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpNumber + "");

            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                ProgressDialog.hideProgressDio();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, FinalKeywordClass.Signincreditional_success);
                                ScankBarMethod.showSnack(getCurrentFocus(), FinalKeywordClass.MobileVerification_successful);
                                FirebaseUser user = task.getResult().getUser();
                                FirebaseAuth.getInstance().signOut();

                                changePasswordAPi();

                            } else {
                                ProgressDialog.hideProgressDio();
                                // Sign in failed, display a message and update the UI
                                Log.w(TAG, FinalKeywordClass.SignInWithCredential_Failure, task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    ScankBarMethod.showSnack(forgotBinding.llParent, getResources().getString(R.string.enter_valid_otp) + "");
                                    // The verification code entered was invalid
                                }
                            }
                        }
                    });

        } else {

            ProgressDialog.hideProgressDio();
            ScankBarMethod.showSnack(forgotBinding.llParent, FinalKeywordClass.VerificationcodenotRecive);
//                Toast.makeText(this, "Verification code is not received", Toast.LENGTH_SHORT).show();
        }

    }


    private void changePasswordAPi() {

        if (IsOnLineMethod.isOnline(ForgotPasswordActivity.this)) {
            serverRequestForForgotPassword();


        } else {
            ScankBarMethod.showSnack(forgotBinding.llParent, getResources().getString(R.string.check_internet_connection));
        }

    }


    private void serverRequestForForgotPassword() {

        ProgressDialog.showProgressDio(ForgotPasswordActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(ForgotPasswordActivity.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapforgotpassword = new HashMap();

        mapforgotpassword.put(ApiClass.getmApiClass().COUNTRY_ID, countryId);
        mapforgotpassword.put(ApiClass.getmApiClass().PHONE, forgotBinding.etMobilenumber.getText().toString());
        mapforgotpassword.put(ApiClass.getmApiClass().PASSWORD, cofirmpassword.toString());


        Call<ForgotResponse> call = mApiInterface.resetpassword(mapforgotpassword);
        call.enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {

                ProgressDialog.hideProgressDio();
                if (response.isSuccessful()) {


                    Log.v("Suceess:", response.body().getMessage().toString());
                    onBackPressed();
                    finish();

                } else {
                    Log.v("Response:", "" + response.message().toString());

                }

            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable t) {
                ProgressDialog.hideProgressDio();
                Log.v("OnFialure:", t + "");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_submit:
                countrycode = forgotBinding.etCountrycode.getText().toString();

                mobilenumber = forgotBinding.etMobilenumber.getText().toString();

                validationforgo_mobile();
                break;
            case R.id.et_countrycode:
                startActivity(new Intent(ForgotPasswordActivity.this, Country_CodeListActivity.class));

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 105) {

            countrycode = data.getExtras().getString(FinalKeywordClass.CountryCode);
            country = data.getExtras().getString(FinalKeywordClass.CountryName);
            countryId = data.getExtras().getString(FinalKeywordClass.CountryId);
            forgotBinding.etCountrycode.setText(plush + countrycode);

        } else {

        }
    }
}
