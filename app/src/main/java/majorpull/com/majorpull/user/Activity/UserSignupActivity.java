package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;

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
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.OtpResponse;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityUsersignupBinding;
import majorpull.com.majorpull.user.Adapter.CountryAdapter;
import majorpull.com.majorpull.user.DataModel.CountryModel;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.Country_CodeListActivity;
import majorpull.com.majorpull.util.DebugLog;
import majorpull.com.majorpull.util.EmailValidation;
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

public class UserSignupActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 007;
    ActivityUsersignupBinding usersignupBinding;
    private String mobileNumber, email, firstname, lastname, profilelink, country, countryId="101", countrycode, password, confirm_password;
    private String TAG = "";
    private String countryList[] = {"India", "Austrelia", "Japan"};
    private String countryCodeList[] = {"1", "2", "3"};


    //    Facebook Containt
    private CallbackManager callbackManager;
    private int resultcode = -1, request_code = 0;
    private int fbcode = 112;
    private String name, social_id, link, emailaddress, Social_id, social_type, passwordfb;
    private GoogleApiClient mGoogleApiClient;
    private CountryAdapter mCountryAdapter;
    private ArrayList<CountryModel> mlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        usersignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_usersignup);

        setFacebook_sdk();
        Bundle i = getIntent().getExtras();

        if (CommonMethod.getPrefrence(UserSignupActivity.this, "UserLogin").equalsIgnoreCase("FbLogin")) {

            firstname = i.getString(FinalKeywordClass.FNAME);
            lastname = i.getString(FinalKeywordClass.LNAME);
            email = i.getString(FinalKeywordClass.EMAIL);
            profilelink = i.getString(FinalKeywordClass.PROFILE_LINK);
            social_id = i.getString(FinalKeywordClass.SOCIAL_ID);
            social_type = i.getString(FinalKeywordClass.Social_TYPE);


            usersignupBinding.etFname.setText(firstname);
            usersignupBinding.etLname.setText(lastname);
            usersignupBinding.etEmailaddress.setText(email);


            DebugLog.log(1, FinalKeywordClass.FNAME, "" + firstname);
            DebugLog.log(1, FinalKeywordClass.LNAME, "" + lastname);
            DebugLog.log(1, FinalKeywordClass.EMAIL, "" + email);
            DebugLog.log(1, FinalKeywordClass.PROFILE_LINK, "" + profilelink);
            DebugLog.log(1, FinalKeywordClass.SOCIAL_ID, "" + social_id);
            DebugLog.log(1, FinalKeywordClass.Social_TYPE, "" + social_type);


        } else if (CommonMethod.getPrefrence(UserSignupActivity.this, "UserLogin").equalsIgnoreCase("GplusLogin")) {
            firstname = i.getString(FinalKeywordClass.FNAME);
            lastname = i.getString(FinalKeywordClass.LNAME);
            email = i.getString(FinalKeywordClass.EMAIL);
            profilelink = i.getString(FinalKeywordClass.PROFILE_LINK);
            usersignupBinding.etFname.setText(firstname);
            usersignupBinding.etLname.setText(lastname);
            usersignupBinding.etEmailaddress.setText(email);


        } else {
            //            blank

        }


        usersignupBinding.ivFb.setOnClickListener(this);
        usersignupBinding.tvSignin.setOnClickListener(this);
        usersignupBinding.tvSignup.setOnClickListener(this);
        usersignupBinding.ivShowPassword.setOnClickListener(this);
        usersignupBinding.ivShowConfirmpassword.setOnClickListener(this);
        usersignupBinding.ivGplash.setOnClickListener(this);
        usersignupBinding.etCountry.setOnClickListener(this);
        usersignupBinding.etCountrycode.setOnClickListener(this);


//        CustomCountryCodeAdapter customAdapter = new CustomCountryCodeAdapter(getApplicationContext(), countryCodeList, countryList);

//        usersignupBinding.tvCountrycode.setAdapter(customAdapter);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(UserSignupActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    private void setFacebook_sdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_password:
                showpaswordMethod(view);
                break;
            case R.id.iv_show_confirmpassword:
                showconfirmpassordMethod(view);
                break;

            case R.id.et_countrycode:
                startActivityForResult(new Intent(getApplicationContext(), Country_CodeListActivity.class), 105);

                break;


            case R.id.et_country:
                startActivityForResult(new Intent(getApplicationContext(), Country_CodeListActivity.class), 105);

                break;
            case R.id.tv_signup:

                signupValidation();
//                startActivity(new Intent(getApplicationContext(), OTPVerificationActivitiy.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                finish();

                break;
            case R.id.iv_fb:
                resultcode = fbcode;
                request_code = 101;
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));


                break;
            case R.id.iv_gplash:

                gplushLogin();
                break;
            case R.id.tv_signin:
                onBackPressed();
                finish();
                break;
        }


    }

    private void gplushLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.e("signin id", RC_SIGN_IN + "");
    }

    //    SignUPValidation Method
    private void signupValidation() {

        mobileNumber = usersignupBinding.etMobilenumber.getText().toString();
        email = usersignupBinding.etEmailaddress.getText().toString();
        firstname = usersignupBinding.etFname.getText().toString();
        lastname = usersignupBinding.etLname.getText().toString();
        country = usersignupBinding.etCountry.getText().toString();
        password = usersignupBinding.etPassword.getText().toString();
        confirm_password = usersignupBinding.etCofirmpassword.getText().toString();


        if (usersignupBinding.etMobilenumber.length() < 1) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.mobilevalidation));
//            usersignupBinding.etMobilenumber.setError(getResources().getString(R.string.mobilevalidation));
        } else if ((usersignupBinding.etMobilenumber.length() < 2) || (usersignupBinding.etMobilenumber.length() < 10)) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.contact_maxlength));
//            usersignupBinding.etMobilenumber.setError(getResources().getString(R.string.contact_maxlength));
        } else if (!(usersignupBinding.etEmailaddress.length() > 0)) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_email));
//            usersignupBinding.etEmailaddress.setError(getResources().getString(R.string.enter_email));
        } else if (!EmailValidation.checkEmail(email)) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.invalid_email));
//            usersignupBinding.etEmailaddress.setError(getResources().getString(R.string.invalid_email));
        } else if (!(firstname.length() > 0)) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_firstname));
//            usersignupBinding.etFname.setError(getResources().getString(R.string.enter_firstname));
        } else if (!(lastname.length() > 0)) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.enter_last_name));
//            usersignupBinding.etLname.setError(getResources().getString(R.string.enter_last_name));
        } else if (!(usersignupBinding.etCountry.length() > 0)) {
//            usersignupBinding.etCountry.setError(getResources().getString(R.string.enter_country));
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.select_country));
        } else if (!(password.length() > 0)) {

            usersignupBinding.etPassword.setError(getResources().getString(R.string.enter_password));
        } else if (password.length() == 0) {
            usersignupBinding.etPassword.setError(getResources().getString(R.string.passblank));
            usersignupBinding.etPassword.requestFocus();
        } else if (password.length() <= 7) {
            usersignupBinding.etPassword.setError(getResources().getString(R.string.pass_mini_length));

        } else if (confirm_password.length() == 0) {
            usersignupBinding.etCofirmpassword.setError(getResources().getString(R.string.enter_cofirmpassword));
            usersignupBinding.etCofirmpassword.requestFocus();
        } else if (confirm_password.length() <= 7) {
            usersignupBinding.etCofirmpassword.setError(getResources().getString(R.string.pass_mini_length));
        } else if (!usersignupBinding.etPassword.getText().toString().trim().equalsIgnoreCase(usersignupBinding.etCofirmpassword.getText().toString().trim())) {
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.password_not_match));
        }
        //net check
        else if (IsOnLineMethod.isOnline(this)) {
            //check user api
//            serverRequestForSignup();
            call_signupApi();
        } else {
            //show net connection message
            //snackbar
            ScankBarMethod.showSnack(getCurrentFocus(), getResources().getString(R.string.check_internet_connection));

        }
    }

    private void call_signupApi() {


        ProgressDialog.showProgressDio(UserSignupActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(UserSignupActivity.this).getUserToken()).
                create(ApiInterface.class);
        Map mapsignup = new HashMap();
        mapsignup.put(ApiClass.getmApiClass().FIRSTNAME, usersignupBinding.etFname.getText().toString());
        mapsignup.put(ApiClass.getmApiClass().LASTNAME, usersignupBinding.etLname.getText().toString().trim());
        mapsignup.put(ApiClass.getmApiClass().EMAIL, usersignupBinding.etEmailaddress.getText().toString().trim());
        mapsignup.put(ApiClass.getmApiClass().PHONE, usersignupBinding.etMobilenumber.getText().toString().trim());
        mapsignup.put(ApiClass.getmApiClass().COUNTRY_ID, countryId);
        mapsignup.put(ApiClass.getmApiClass().PASSWORD, usersignupBinding.etPassword.getText().toString().trim());
        mapsignup.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        mapsignup.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);


        Call<OtpResponse> call = mApiInterface.getOtp(mapsignup);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                ProgressDialog.hideProgressDio();
                if (response.isSuccessful()) {
                    OtpResponse mOtpResponse = response.body();
                    if (mOtpResponse != null) {

                        Intent i = new Intent(UserSignupActivity.this, OTPVerificationActivitiy.class);
                        i.putExtra(FinalKeywordClass.COMINGFROM, "signup");

                        i.putExtra(FinalKeywordClass.FNAME, usersignupBinding.etFname.getText().toString());
                        i.putExtra(FinalKeywordClass.LNAME, usersignupBinding.etLname.getText().toString().trim());
                        i.putExtra(FinalKeywordClass.EMAIL, usersignupBinding.etEmailaddress.getText().toString().trim());
                        i.putExtra(FinalKeywordClass.CountryId, countryId);
                        i.putExtra(FinalKeywordClass.CountryCode, usersignupBinding.etCountrycode.getText().toString());
                        i.putExtra(FinalKeywordClass.CONTACT, usersignupBinding.etMobilenumber.getText().toString().trim());
                        i.putExtra(FinalKeywordClass.PASSWORD, password + "");
                        i.putExtra(FinalKeywordClass.SOCIAL_ID, social_id);
                        i.putExtra(FinalKeywordClass.Social_TYPE, social_type);
                        startActivity(i);

                    } else {
                        //show error about response
                        //somthing went wrong
//                        DebugLog.log(1, FinalKeywordClass.RESPONSE, "Something_Wrong");
                        ScankBarMethod.showSnack(getCurrentFocus(), response.message().toString());
                    }
                } else {
                    try {
                        ProgressDialog.hideProgressDio();
                        System.out.println(response.errorBody().string().toString());
                        //show error about response
                        //somthing went wrong
                        ScankBarMethod.showSnack(getCurrentFocus(), response.errorBody().string().toString() + "");
//                        DebugLog.log(1, FinalKeywordClass.RESPONSE, response.errorBody().string().toString() + "");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                t.printStackTrace();
                ProgressDialog.hideProgressDio();
                ScankBarMethod.showSnack(getCurrentFocus(), t + "");
//                DebugLog.log(1, "OnFailure", t + "");


            }
        });

    }


    private void showconfirmpassordMethod(View v) {

        if (usersignupBinding.ivShowConfirmpassword.isSelected()) {
            usersignupBinding.etCofirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            v.setSelected(false);
            usersignupBinding.ivShowConfirmpassword.setSelected(false);
            usersignupBinding.ivShowConfirmpassword.setImageResource(R.drawable.paswd_reset);

        } else {
            usersignupBinding.etCofirmpassword.setInputType(InputType.TYPE_CLASS_TEXT);
            usersignupBinding.ivShowConfirmpassword.setImageResource(R.drawable.paswd_eye);
            usersignupBinding.ivShowConfirmpassword.setSelected(true);

            v.setSelected(true);
        }

    }


    //    show or hide password
    private void showpaswordMethod(View v) {
        if (usersignupBinding.ivShowPassword.isSelected()) {
            usersignupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            usersignupBinding.ivShowPassword.setImageResource(R.drawable.paswd_reset);

            v.setSelected(false);
        } else {
            usersignupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            usersignupBinding.ivShowPassword.setImageResource(R.drawable.paswd_eye);

            v.setSelected(true);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            RequestFbData();
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
            Log.e("signinresult :", result + "" + "\nresultcode =>" + resultCode + "");
        } else if (requestCode == 105) {
            if (resultCode == RESULT_OK) {
                countrycode = data.getExtras().getString(FinalKeywordClass.CountryCode);
                country = data.getExtras().getString(FinalKeywordClass.CountryName);
                countryId = data.getExtras().getString(FinalKeywordClass.CountryId);
                usersignupBinding.etCountrycode.setText(countrycode);
                usersignupBinding.etCountry.setText(country);


            }
        }
    }

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
                    CommonMethod.setPrefrence(UserSignupActivity.this, "UserLogin", "GplusLogin");
                    Intent googleintent = new Intent(UserSignupActivity.this, OTPVerificationActivitiy.class);

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
                    CommonMethod.setPrefrence(UserSignupActivity.this, "UserLogin", "GplusLogin");
                    Intent googleintent = new Intent(UserSignupActivity.this, OTPVerificationActivitiy.class);

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

                CommonMethod.setPrefrence(UserSignupActivity.this, "UserLogin", "GplusLogin");
                Intent googleintent = new Intent(UserSignupActivity.this, OTPVerificationActivitiy.class);

                googleintent.putExtra(FinalKeywordClass.FNAME, firstname);
                googleintent.putExtra(FinalKeywordClass.LNAME, lastname);
                googleintent.putExtra(FinalKeywordClass.EMAIL, email);
                googleintent.putExtra(FinalKeywordClass.PROFILE_LINK, personPhotoUrl + "");
                googleintent.putExtra(FinalKeywordClass.SOCIAL_ID, FinalKeywordClass.GOOGLE_PLUSH_ID);
                googleintent.putExtra(FinalKeywordClass.Social_TYPE, FinalKeywordClass.GOOGLE_PLUSH);
                startActivity(googleintent);
                finish();

                DebugLog.log(1, "GmailLogin", "Image not inserted");
//                Intent intent = new Intent(MainActivity.this, GPlushActivity.class);
//                intent.putExtra("username", personName);
//                intent.putExtra("email", email);
//                intent.putExtra("img", R.drawable.dummyu);
//                startActivity(intent);
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

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.v("response", response.toString());
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {

                        name = json.getString("name");
                        social_id = json.getString("id");

                        if (json.getJSONObject("picture").getJSONObject("data") != null)
                            link = json.getJSONObject("picture").getJSONObject("data").getString("url");
                        else
                            link = "";
                        if (object.has("email")) {
                            emailaddress = object.getString("email");
                        } else {
                            emailaddress = "";
                        }

                        social_type = "FACEBOOK";
                        passwordfb = "";
                        Log.e("data:", "\nName:" + name + "\nSocialId:" + social_id + "\nlink:" + link + "\nemail:" + email);


                        usersignupBinding.etFname.setText(name);


                        usersignupBinding.etEmailaddress.setText(email);


                        DebugLog.log(1, "", "");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.width(150).height(150),birthday");
        request.setParameters(parameters);
        request.executeAsync();
        Log.v("requestdata=>", request + "");
//        response.setText(request+"");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


}
