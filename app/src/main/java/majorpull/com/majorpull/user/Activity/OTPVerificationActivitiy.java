package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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
import ResultResponse.SignUpResponse;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityOtpVerifacationBinding;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.DebugLog;
import majorpull.com.majorpull.util.FinalKeywordClass;
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

public class OTPVerificationActivitiy extends AppCompatActivity implements View.OnClickListener {


    private ActivityOtpVerifacationBinding otpBinding;

    private String firstname, lastname, email, countryId, password, social_id, social_type, country_code, country, mobilenumber="";
    private com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;
    private String TAG = "1234";

    private String mVerificationId = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private String verificationId = "", code = "";
    private String update_key, comingfrom = "";


    //    private FirebaseAuth mAuth;
//    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verifacation);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        Intent intent = getIntent();


        if (getIntent().getStringExtra(FinalKeywordClass.COMINGFROM).equalsIgnoreCase("profile")) {
            //iam comming from profile page

            try {
                comingfrom = intent.getStringExtra(FinalKeywordClass.COMINGFROM);

                firstname = intent.getStringExtra(FinalKeywordClass.FNAME);
                lastname = intent.getStringExtra(FinalKeywordClass.LNAME);
                email = intent.getStringExtra(FinalKeywordClass.EMAIL);
                countryId = intent.getStringExtra(FinalKeywordClass.CountryId);
                country_code = intent.getStringExtra(FinalKeywordClass.CountryCode);
                mobilenumber = intent.getStringExtra(FinalKeywordClass.CONTACT);

                DebugLog.log(1, "CountryCode:", country_code + mobilenumber);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (getIntent().getStringExtra(FinalKeywordClass.COMINGFROM).equalsIgnoreCase("signup")) {
            //iam comming from signup page

            try {
                comingfrom = intent.getStringExtra(FinalKeywordClass.COMINGFROM);

                firstname = intent.getStringExtra(FinalKeywordClass.FNAME);
                lastname = intent.getStringExtra(FinalKeywordClass.LNAME);
                email = intent.getStringExtra(FinalKeywordClass.EMAIL);
                countryId = intent.getStringExtra(FinalKeywordClass.CountryId);
                country_code = intent.getStringExtra(FinalKeywordClass.CountryCode);
                mobilenumber = intent.getStringExtra(FinalKeywordClass.CONTACT);
                password = intent.getStringExtra(FinalKeywordClass.PASSWORD);
                social_id = intent.getStringExtra(FinalKeywordClass.SOCIAL_ID);
                social_type = intent.getStringExtra(FinalKeywordClass.Social_TYPE);

                DebugLog.log(1, "CountryCode:", country_code + mobilenumber);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        otpBinding.tvVerify.setOnClickListener(this);
        otpBinding.tvResendOtp.setOnClickListener(this);
        setUpFocusWork();
        firebaseOTP(true);
    }

    private void firebaseOTP(boolean isFirstTime) {

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
        phone_auth.verifyPhoneNumber("+" + country_code + mobilenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        DebugLog.log(1, "CountryCode:", country_code + mobilenumber);


       /* } else {
            phone_auth.verifyPhoneNumber(
                    country_code + "" + mobilenumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks, mResendToken);
        }*/


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


    void setUpFocusWork() {
        otpBinding.etOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpBinding.etOne.getText().toString().trim().length() > 0) {
                    otpBinding.etOne.clearFocus();
                    otpBinding.etTwo.requestFocus();
                }
            }
        });
        otpBinding.etTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpBinding.etTwo.getText().toString().trim().length() > 0) {
                    otpBinding.etTwo.clearFocus();
                    otpBinding.etThree.requestFocus();
                }
                if (otpBinding.etTwo.getText().toString().trim().equals("")) {
                    otpBinding.etTwo.clearFocus();
                    otpBinding.etOne.requestFocus();
                }
            }
        });
        otpBinding.etThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpBinding.etThree.getText().toString().trim().length() > 0) {
                    otpBinding.etThree.clearFocus();
                    otpBinding.etFour.requestFocus();
                }
                if (otpBinding.etThree.getText().toString().trim().equals("")) {
                    otpBinding.etThree.clearFocus();
                    otpBinding.etTwo.requestFocus();
                }
            }
        });
        otpBinding.etFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (otpBinding.etFour.getText().toString().trim().length() > 0) {
                    otpBinding.etFour.clearFocus();
                    otpBinding.etFive.requestFocus();
                }
                if (otpBinding.etFour.getText().toString().trim().equals("")) {
                    otpBinding.etFour.clearFocus();
                    otpBinding.etThree.requestFocus();
                }


            }
        });

        otpBinding.etFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpBinding.etFive.getText().toString().trim().length() > 0) {
                    otpBinding.etFive.clearFocus();
                    otpBinding.etSix.requestFocus();
                }
                if (otpBinding.etFive.getText().toString().trim().equals("")) {
                    otpBinding.etFive.clearFocus();
                    otpBinding.etFour.requestFocus();
                }

            }
        });

        otpBinding.etSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpBinding.etSix.getText().toString().trim().equals("")) {
                    otpBinding.etSix.clearFocus();
                    otpBinding.etFive.requestFocus();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_verify:

                SixDigitVerification();
//                verifyOtpMethod();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
                break;

            case R.id.tv_resend_otp:

                ProgressDialog.showProgressDio(OTPVerificationActivitiy.this);
                firebaseOTP(false);
                clearOTP();


                break;
        }
    }

    //    FourDigitsVerification
    private void SixDigitVerification() {


        if (otpBinding.etOne.length() != 0 && otpBinding.etTwo.length() != 0 && otpBinding.etThree.length() != 0
                && otpBinding.etFour.length() != 0) {

            ProgressDialog.showProgressDio(OTPVerificationActivitiy.this);
            if (mVerificationId != null && !mVerificationId.equalsIgnoreCase("")) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpBinding.etOne.getText().toString() + "" +
                        otpBinding.etTwo.getText().toString() + "" + otpBinding.etThree.getText().toString() + "" +
                        otpBinding.etFour.getText().toString() + "" +
                        otpBinding.etFive.getText().toString() + "" +
                        otpBinding.etSix.getText().toString());
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


                                    otpBinding.etOne.setEnabled(false);
                                    otpBinding.etTwo.setEnabled(false);
                                    otpBinding.etThree.setEnabled(false);
                                    otpBinding.etFour.setEnabled(false);
                                    otpBinding.etFive.setEnabled(false);
                                    otpBinding.etSix.setEnabled(false);

                                    otpBinding.etOne.setFocusable(false);
                                    otpBinding.etOne.setClickable(false);

                                    otpBinding.etTwo.setFocusable(false);
                                    otpBinding.etTwo.setClickable(false);

                                    otpBinding.etThree.setFocusable(false);
                                    otpBinding.etThree.setClickable(false);

                                    otpBinding.etFour.setFocusable(false);
                                    otpBinding.etFour.setClickable(false);

                                    otpBinding.etFive.setFocusable(false);
                                    otpBinding.etFive.setClickable(false);

                                    otpBinding.etSix.setFocusable(false);
                                    otpBinding.etSix.setClickable(false);

                                    //signup api here

                                    connectionValidation();

                                } else {
                                    ProgressDialog.hideProgressDio();
                                    // Sign in failed, display a message and update the UI
                                    Log.w(TAG, FinalKeywordClass.SignInWithCredential_Failure, task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_valid_otp) + "");
                                        // The verification code entered was invalid
                                    }
                                }
                            }
                        });


            } else {
                ProgressDialog.hideProgressDio();
                ScankBarMethod.showSnack(getCurrentFocus(), FinalKeywordClass.VerificationcodenotRecive);
//                Toast.makeText(this, "Verification code is not received", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void connectionValidation() {

        if (IsOnLineMethod.isOnline(OTPVerificationActivitiy.this)) {

            if (comingfrom.equalsIgnoreCase("profile")) {
                //run profile update api
                call_update_commingProfile();

            } else if (comingfrom.equalsIgnoreCase("signup")) {
                call_SignupApi();
            }
        } else {
            //error net disconneti
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.check_internet_connection));

        }

    }

    private void call_update_commingProfile() {

        ProgressDialog.showProgressDio(OTPVerificationActivitiy.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(OTPVerificationActivitiy.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapsignup = new HashMap();

        mapsignup.put(ApiClass.getmApiClass().FIRSTNAME, firstname);
        mapsignup.put(ApiClass.getmApiClass().LASTNAME, lastname);
        mapsignup.put(ApiClass.getmApiClass().EMAIL, email);
        mapsignup.put(ApiClass.getmApiClass().PHONE, mobilenumber);
        mapsignup.put(ApiClass.getmApiClass().COUNTRY_ID, countryId);
        mapsignup.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        mapsignup.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);

        Call<SignUpResponse> call = mApiInterface.getSginup(mapsignup);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                if (response.isSuccessful()) {

                    SignUpResponse msignup_response = response.body();

                    Log.v("Response:", msignup_response + "");
                    if (msignup_response != null) {

                        SavePrefrence.getInstance(OTPVerificationActivitiy.this).saveUserdetail(msignup_response.getData());
                        finish();

                    } else {
                        //show error about response
                        //somthing went wrong
//                        DebugLog.log(1, FinalKeywordClass.RESPONSE, "Something_Wrong");
                        ScankBarMethod.showSnack(getCurrentFocus(), response.message().toString());

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
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

                t.printStackTrace();
                ProgressDialog.hideProgressDio();
                DebugLog.log(1, "OnFailure", t + "");

            }
        });


    }

    private void call_SignupApi() {


        ProgressDialog.showProgressDio(OTPVerificationActivitiy.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(OTPVerificationActivitiy.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapsignup = new HashMap();

        mapsignup.put(ApiClass.getmApiClass().FIRSTNAME, firstname);
        mapsignup.put(ApiClass.getmApiClass().LASTNAME, lastname);
        mapsignup.put(ApiClass.getmApiClass().EMAIL, email);
        mapsignup.put(ApiClass.getmApiClass().PHONE, mobilenumber);
        mapsignup.put(ApiClass.getmApiClass().COUNTRY_ID, countryId);
        mapsignup.put(ApiClass.getmApiClass().PASSWORD, password);
        mapsignup.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        mapsignup.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);

        Call<SignUpResponse> call = mApiInterface.getSginup(mapsignup);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                if (response.isSuccessful()) {

                    SignUpResponse msignup_response = response.body();

                    Log.v("Response:", msignup_response + "");
                    if (msignup_response != null) {

                        SavePrefrence.getInstance(OTPVerificationActivitiy.this).saveUserdetail(msignup_response.getData());

                        startActivity(new Intent(OTPVerificationActivitiy.this, MainActivity.class));
                        finish();

                    } else {
                        //show error about response
                        //somthing went wrong
//                        DebugLog.log(1, FinalKeywordClass.RESPONSE, "Something_Wrong");
                        ScankBarMethod.showSnack(getCurrentFocus(), response.message().toString());

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
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

                t.printStackTrace();
                ProgressDialog.hideProgressDio();
                DebugLog.log(1, "OnFailure", t + "");

            }
        });


    }

    private void verifyOtpMethod() {

        if (IsOnLineMethod.isOnline(OTPVerificationActivitiy.this)) {
            get_User_SignUp(mobilenumber);
        } else {

            //check internet connection
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.check_internet_connection));
        }

    }

    //    SignUpApi
    private void get_User_SignUp(String mobilenumber) {
        DebugLog.log(1, FinalKeywordClass.CONTACT, mobilenumber);

//        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    //    UpdateApi
    private void user_UpdateMethod(String mobilenumber) {

    }


    //    ClearOTPMethod
    private void clearOTP() {
        otpBinding.etOne.setText("");
        otpBinding.etTwo.setText("");
        otpBinding.etThree.setText("");
        otpBinding.etFour.setText("");
        otpBinding.etFive.setText("");
        otpBinding.etSix.setText("");
        otpBinding.etOne.requestFocus();
    }
}
