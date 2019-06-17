package majorpull.com.majorpull.user.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.SignINResponce;
import ResultResponse.Userdata;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityUserloginBinding;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.Country_CodeListActivity;
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

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private ActivityUserloginBinding binding;
    private boolean is_selected = false;
    private CallbackManager callbackManager;
    private int resultcode = -1, request_code = 0;
    private int fbcode = 112;
    private String firstname, lastname, name, mobileNumber, social_id, link, emailaddress, password, social_type, passwordfb, countrycode = "", plush = "+", country, countryId = "101";
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 007;
    private static final String TAG = "UserLoginActivity)";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_userlogin);

        setFacebook_sdk();
        binding.tvForgot.setOnClickListener(this);
        binding.tvSignin.setOnClickListener(this);
        binding.tvSignup.setOnClickListener(this);
        binding.tvFbSignin.setOnClickListener(this);
        binding.ivShow.setOnClickListener(this);
        binding.tvGplushSignin.setOnClickListener(this);
        binding.etCountrycode.setOnClickListener(this);
//        binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(UserLoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    private void setFacebook_sdk() {

        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

    }


    private void call_LoginApi() {


        ProgressDialog.showProgressDio(UserLoginActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(UserLoginActivity.this).getUserToken()).
                create(ApiInterface.class);
        final Map mapsignup = new HashMap();

        mapsignup.put(ApiClass.getmApiClass().PHONE, binding.etMobilenumber.getText().toString());
        mapsignup.put(ApiClass.getmApiClass().COUNTRYCODE, binding.etCountrycode.getText().toString());
        mapsignup.put(ApiClass.getmApiClass().PASSWORD, binding.etPassword.getText().toString());
        mapsignup.put(ApiClass.getmApiClass().COUNTRY_ID, countryId);
        mapsignup.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        mapsignup.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);


        Call<SignINResponce> call = mApiInterface.getSignin(mapsignup);
        call.enqueue(new Callback<SignINResponce>() {
            @Override
            public void onResponse(Call<SignINResponce> call, Response<SignINResponce> response) {
                ProgressDialog.hideProgressDio();
                if (response.isSuccessful()) {

                    SignINResponce msignup_response = response.body();

                    Log.v("Response:", msignup_response + "");
                    if (msignup_response != null && msignup_response.getStatus().equalsIgnoreCase("true")) {

                        SavePrefrence.getInstance(UserLoginActivity.this).saveUserdetail(msignup_response.getData());
                        startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        //show error about response
                        //somthing went wrong
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
            public void onFailure(Call<SignINResponce> call, Throwable t) {

                t.printStackTrace();
                ProgressDialog.hideProgressDio();
                DebugLog.log(1, "OnFailure", t + "");

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signup:
                CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "blank");

                startActivity(new Intent(getApplicationContext(), UserSignupActivity.class));
                break;
            case R.id.tv_forgot:
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                break;

            case R.id.et_countrycode:
                startActivityForResult(new Intent(UserLoginActivity.this, Country_CodeListActivity.class), 105);

                break;

            case R.id.tv_signin:


                signinvalidation();


                // startActivity(new Intent(getApplicationContext(), MainActivity.class));

                break;
            case R.id.iv_show:

                showpaswordMethod(v);
                break;

            case R.id.tv_fb_signin:

                resultcode = fbcode;
                request_code = 101;
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

                break;

            case R.id.tv_gplush_signin:

                gplushLogin();
//                gogglecontected();
                break;
        }

    }

    private void signinvalidation() {

        countrycode = binding.etCountrycode.getText().toString();
        countrycode = binding.etCountrycode.getText().toString();
        mobileNumber = binding.etMobilenumber.getText().toString();
        password = binding.etPassword.getText().toString();

        if (countrycode.length() < 1) {
            binding.etCountrycode.setError(getResources().getString(R.string.count_code_error));
//            ScankBarMethod.showSnack(binding.llParent, getResources().getString(R.string.count_code_error));
        } else if (mobileNumber.length() < 1) {
            binding.etMobilenumber.setError(getResources().getString(R.string.mobilevalidation));
//            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.mobilevalidation));
        } else if (mobileNumber.length() < 7) {
//            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.contact_maxlength));
            binding.etMobilenumber.setError(getResources().getString(R.string.mobileno_mini_length_error));
        } else if (mobileNumber.length() > 15) {
            binding.etMobilenumber.setError(getResources().getString(R.string.mobileno_mini_length_error));
        } else if (password.length() == 0) {
            binding.etPassword.setError(getResources().getString(R.string.enter_password));

//            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_password));

        } else if (password.length() <= 7) {
            binding.etPassword.setError(getResources().getString(R.string.pass_mini_length));
//            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.pass_mini_length));

        } else if (IsOnLineMethod.isOnline(UserLoginActivity.this)) {

            call_LoginApi();

        } else {
            ScankBarMethod.showSnack(binding.llParent, getResources().getString(R.string.check_internet_connection));
        }

    }

    private void gplushLogin() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.e("signin id", RC_SIGN_IN + "");

    }


    private void gogglecontected() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//        }


    }


    //    show or hide password
    private void showpaswordMethod(View v) {
        if (binding.ivShow.isSelected()) {

            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.ivShow.setSelected(false);
//            v.setSelected(false);
            binding.ivShow.setImageResource(R.drawable.paswd_reset);
            Log.v("Reset", "RestView");


        } else {
            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.ivShow.setSelected(true);
//            v.setSelected(true);
            binding.ivShow.setImageResource(R.drawable.paswd_eye);
            Log.v("Eye", "EyeView");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (request_code == 101) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            RequestFbData();
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
            Log.e("signinresult :", result + "" + "\nresultcode =>" + resultCode + "");
        } else if (requestCode == 105) {

            countrycode = data.getExtras().getString(FinalKeywordClass.CountryCode);
            country = data.getExtras().getString(FinalKeywordClass.CountryName);
            countryId = data.getExtras().getString(FinalKeywordClass.CountryId);
            binding.etCountrycode.setText(plush + countrycode);

        }
    }

    @SuppressLint("LongLogTag")
    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, FinalKeywordClass.DisplayName + acct.getDisplayName());
            //btnSignIn.setVisibility(View.GONE);

            Log.e(FinalKeywordClass.SuccessConnection, mGoogleApiClient.isConnected() + ""); //First Time log in false default
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String personPhotoUrl = "";
            String socialType = acct.getAccount().toString();
            String socialTypee = acct.getAccount().type;
            String socialId = acct.getId();

            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
                Log.e(TAG, "Name:" + personName + ", email:" + email + ", Image:" + personPhotoUrl);

                String fullname = acct.getDisplayName();
                String[] parts = fullname.split("\\s+");
                Log.d("Length-->", "" + parts.length);

//                UserName Divided Into Two Part Code
                if (parts.length == 2) {
                    String firstname = parts[0];
                    String lastname = parts[1];
                    Log.d("First-->", "" + firstname);
                    Log.d("Last-->", "" + lastname);
                    Log.d(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.GOOGLE_PLUSH_ID);
                    Log.d(FinalKeywordClass.Social_TYPE, FinalKeywordClass.GOOGLE_PLUSH);
//                    Log.d(FinalKeywordClass.SOCIAL_ID, socialId + "");
//                    Log.d(FinalKeywordClass.Social_TYPE, socialType + "");
//                    Log.d(FinalKeywordClass.Social_TYPEE, socialTypee + "");
                    CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "GplusLogin");
                    Intent googleintent = new Intent(UserLoginActivity.this, UserSignupActivity.class);

                    googleintent.putExtra(FinalKeywordClass.FNAME, firstname);
                    googleintent.putExtra(FinalKeywordClass.LNAME, lastname);
                    googleintent.putExtra(FinalKeywordClass.EMAIL, email);
                    googleintent.putExtra(FinalKeywordClass.PROFILE_LINK, personPhotoUrl);
                    googleintent.putExtra(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.GOOGLE_PLUSH_ID);
                    googleintent.putExtra(FinalKeywordClass.Social_TYPE, FinalKeywordClass.GOOGLE_PLUSH);
                    startActivity(googleintent);
                    finish();
//                    AppController.setStringPref("firstnamebook", firstname);
//                    AppController.setStringPref("lastnamebook", lastname);
//
//                    Log.d("FirstApp", "" + AppController.getStringPref("firstnamebook"));
//                    Log.d("LastApp", "" + AppController.getStringPref("lastnamebook"));
                }
//                UseName divided into three part code
                else if (parts.length == 3) {
                    String firstname = parts[0];
                    String middlename = parts[1];
                    String lastname = parts[2];
                    Log.d("First-->", "" + firstname);
                    Log.d("Last-->", "" + lastname);
                    CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "GplusLogin");
                    Intent googleintent = new Intent(UserLoginActivity.this, UserSignupActivity.class);

                    googleintent.putExtra(FinalKeywordClass.FNAME, firstname);
                    googleintent.putExtra(FinalKeywordClass.LNAME, lastname);
                    googleintent.putExtra(FinalKeywordClass.EMAIL, email);
                    googleintent.putExtra(FinalKeywordClass.PROFILE_LINK, personPhotoUrl);
                    googleintent.putExtra(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.GOOGLE_PLUSH_ID);
                    googleintent.putExtra(FinalKeywordClass.Social_TYPE, FinalKeywordClass.GOOGLE_PLUSH);
                    startActivity(googleintent);
                    finish();
//                    AppController.setStringPref("firstnamebook", firstname);
//                    AppController.setStringPref("lastnamebook", lastname);
                }

//                CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "GplusLogin");
//                Intent googleintent = new Intent(UserLoginActivity.this, UserSignupActivity.class);
//
//                googleintent.putExtra("NAME", personName);
//                googleintent.putExtra("EMAIL", email);
//                googleintent.putExtra("IMGLINK", personPhotoUrl);
//                startActivity(googleintent);
//                finish();

//                Intent intent = new Intent(MainActivity.this, GPlushActivity.class);
//                intent.putExtra("username", personName);
//                intent.putExtra("email", email);
//                intent.putExtra("img", personPhotoUrl);
//                startActivity(intent);
//                finish();
            } else {

                CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "GplusLogin");
                Intent googleintent = new Intent(UserLoginActivity.this, UserSignupActivity.class);

                googleintent.putExtra(FinalKeywordClass.FNAME, firstname);
                googleintent.putExtra(FinalKeywordClass.LNAME, lastname);
                googleintent.putExtra(FinalKeywordClass.EMAIL, email);
                googleintent.putExtra(FinalKeywordClass.PROFILE_LINK, personPhotoUrl + "");
                googleintent.putExtra(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.GOOGLE_PLUSH_ID);
                googleintent.putExtra(FinalKeywordClass.Social_TYPE, FinalKeywordClass.GOOGLE_PLUSH);
                startActivity(googleintent);
                finish();

                DebugLog.log(1, "GmailLogin", "Image not inserted");
            }


            //updateUI(true);
        } else {
            // Log.e("econnection else :",mGoogleApiClient.isConnected()+"");

            // Signed out, show unauthenticated UI.
            // updateUI(false);
            Log.d("Logger", "handleSignInResult : Fail");
        }
    }


    private void RequestFbData() {


        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("response", response.toString());
                        JSONObject json = response.getJSONObject();
                        try {
                            if (json != null) {

                                firstname = json.getString("first_name");
                                lastname = json.getString("last_name");

                                name = json.getString("name");
                                social_id = json.getString("id");

                                if (json.getJSONObject("picture").getJSONObject("data") != null)
                                    link = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                else
                                    link = "";

                                try {


                                    if (object.has("email")) {
                                        emailaddress = object.getString("email");
                                    } else {
                                        emailaddress = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                social_type = "FACEBOOK";
                                passwordfb = "";
                                Log.e("data:", "\nName:" + name + "\nFristName:" + firstname + "\nLastName:" + lastname + "\nSocialId:" + social_id + "\nlink:" + link + "\nemail:" + emailaddress);
                                DebugLog.log(1, "", "");


//                                call check user api


//                                if status true in response then redirect to main page
//                                otherwise open signuppage

                                CommonMethod.setPrefrence(UserLoginActivity.this, "UserLogin", "FbLogin");
                                Intent i = new Intent(getApplicationContext(), UserSignupActivity.class);
                                i.putExtra(FinalKeywordClass.FNAME, firstname);
                                i.putExtra(FinalKeywordClass.LNAME, lastname);
                                i.putExtra(FinalKeywordClass.EMAIL, emailaddress);
                                i.putExtra(FinalKeywordClass.PROFILE_LINK, link);
//                                i.putExtra(FinalKeywordClass.SOCIAL_ID, social_id + "");
//                                i.putExtra(FinalKeywordClass.Social_TYPE, social_type + "");
                                i.putExtra(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.FB_ID);
                                i.putExtra(FinalKeywordClass.Social_TYPE, FinalKeywordClass.FB_Type);
                                startActivity(i);
                                finish();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,first_name,last_name,picture.width(150).height(150),birthday");
        request.setParameters(parameters);
        request.executeAsync();
        Log.v("requestdata=>", request + "");
//        response.setText(request+"");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }
}
