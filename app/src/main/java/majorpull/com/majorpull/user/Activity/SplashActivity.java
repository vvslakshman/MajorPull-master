package majorpull.com.majorpull.user.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.SplashResponce;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivitySplashBinding;
import majorpull.com.majorpull.util.FinalKeywordClass;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String Developer_Email = "developer@major.com";
    private static final String Develper_Password = "123456";
    private String RESULT = "Result =>", mError = "Error=>";

    private ActivitySplashBinding bindsplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {
            if (SavePrefrence.getInstance(this).getUserToken() == null) {
                if (IsOnLineMethod.isOnline(SplashActivity.this)) {
                    getaccessTocken();
                } else {
                    Log.v("interenet", getResources().getString(R.string.check_internet_connection));

                    //error net disconneti

                    ScankBarMethod.showSnack(bindsplash.llParent, getResources().getString(R.string.check_internet_connection));
                }
            } else {
                ///access token not null
                lunchNextActivity();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void lunchNextActivity() {

        if (SavePrefrence.getInstance(SplashActivity.this).getUserdetail() != null) {
            //alreadylogin
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {

            startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
            finish();
        }

    }

    //    getaccess Api
    private void getaccessTocken() {

        ProgressDialog.showProgressDio(SplashActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, null).
                create(ApiInterface.class);
        Map mparamsplash = new HashMap();
        mparamsplash.put(FinalKeywordClass.EMAIL, Developer_Email);
        mparamsplash.put(FinalKeywordClass.PASSWORD, Develper_Password);

        Call<SplashResponce> call = mApiInterface.getsplash(mparamsplash);

        call.enqueue(new Callback<SplashResponce>() {
            @Override
            public void onResponse(Call<SplashResponce> call, Response<SplashResponce> response) {
                ProgressDialog.hideProgressDio();
                if (response.isSuccessful()) {
                    SplashResponce msplashresponce = response.body();

                    if (msplashresponce != null) {

                        //response ok now ican do my work

                        if (msplashresponce.getToken() != null && !msplashresponce.getToken().equalsIgnoreCase(""))

                        {
                            SavePrefrence.getInstance(SplashActivity.this).saveUserToken(msplashresponce.getToken());
                            lunchNextActivity();

                        } else {

                            //show blank token error
                        }


                    } else {
                        //show error about response
                        //somthing went wrong
                    }

                } else {
                    try {
                        System.out.println(response.errorBody().string().toString());
                        //show error about response
                        //somthing went wrong
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SplashResponce> call, Throwable t) {
                t.printStackTrace();
                ProgressDialog.hideProgressDio();
            }
        });
    }
}
