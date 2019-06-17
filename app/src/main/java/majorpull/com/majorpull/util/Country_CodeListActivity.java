package majorpull.com.majorpull.util;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.CountryResponce;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityCountryCodeLayoutBinding;
import majorpull.com.majorpull.user.Adapter.CountryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user102 on 4/10/18.
 */

public class Country_CodeListActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCountryCodeLayoutBinding bindcountry;
    private CountryAdapter mCountryAdapter;
    private String country_code = "", country_name = "", country_id = "";
    private String CounrtrycodeTextchange = "CounrtrycodeTextchange:>";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindcountry = DataBindingUtil.setContentView(this, R.layout.activity_country_code_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Country_CodeListActivity.this);
        bindcountry.countryRecyclerview.setLayoutManager(mLayoutManager);


        //net check
        if (IsOnLineMethod.isOnline(this)) {
            call_countryApi();
        } else {
            //show net connection message
            //snackbar
            ScankBarMethod.showSnack(bindcountry.llParent, getResources().getString(R.string.check_internet_connection));

        }

//


        bindcountry.ivBack.setOnClickListener(this);
        bindcountry.tvOk.setOnClickListener(this);

        bindcountry.serachviewcountry.setActivated(true);
        bindcountry.serachviewcountry.setQueryHint("Type your keyword here");
        bindcountry.serachviewcountry.onActionViewExpanded();
        bindcountry.serachviewcountry.setIconified(false);
        bindcountry.serachviewcountry.clearFocus();
        bindcountry.serachviewcountry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                DebugLog.log(1, "Countrycode", query + "");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCountryAdapter.getFilter().filter(newText);

                DebugLog.log(1, CounrtrycodeTextchange, newText);


                return false;
            }
        });

    }


    CountryResponce mCountryResponce;

    //        CountryApi Call
    private void call_countryApi() {

        ProgressDialog.showProgressDio(Country_CodeListActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(Country_CodeListActivity.this).getUserToken()).
                create(ApiInterface.class);

        Call<CountryResponce> call = mApiInterface.getCountryList();

        call.enqueue(new Callback<CountryResponce>() {
            @Override
            public void onResponse(Call<CountryResponce> call, Response<CountryResponce> response) {
                ProgressDialog.hideProgressDio();
                if (response.isSuccessful()) {
                    mCountryResponce = response.body();
                    if (mCountryResponce != null) {

                        if (mCountryResponce.getStatus() != null && mCountryResponce.getStatus() == true) {
                            //response ok now ican do my work

                            if (mCountryResponce.getDate() != null)

                            {
                                mCountryAdapter = new CountryAdapter(Country_CodeListActivity.this, mCountryResponce.getDate());

                                bindcountry.countryRecyclerview.setAdapter(mCountryAdapter);

                                mCountryAdapter.setOnClickListener(new CountryAdapter.OnclickListener() {
                                    @Override
                                    public void Onposclick(int pos) {

                                        HideKeyboardMethod.hideKeyboard(Country_CodeListActivity.this);
                                        for (int i = 0; i < mCountryResponce.getDate().size(); i++) {
                                            if (pos == i) {
                                                mCountryResponce.getDate().get(i).setIs_checked(true);

                                                country_code = mCountryResponce.getDate().get(i).getPhonecode().toString();
                                                country_name = mCountryResponce.getDate().get(i).getName().toString();
                                                country_id = mCountryResponce.getDate().get(i).getId().toString();

                                            } else {
                                                mCountryResponce.getDate().get(i).setIs_checked(false);
                                            }
                                        }

                                        mCountryAdapter.notifyDataSetChanged();
                                    }
                                });

                            } else {
                                //show blank token error

                            }
                        } else {
                            if (mCountryResponce.getStatus() != null) {
                                //show error about api response
                                //  msplashresponce.getMessage()
                            } else {
                                //show error about response
                                //somthing went wrong
                            }
                        }

                    } else {
                        //show error about response
                        //somthing went wrong
                    }

                } else {
                    try {
                        System.out.println(response.errorBody().string().toString());
                        //show error about response
                        ScankBarMethod.showSnack(getCurrentFocus(), response.errorBody().string().toString());
                        //somthing went wrong
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CountryResponce> call, Throwable t) {
                t.printStackTrace();
                ProgressDialog.hideProgressDio();
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_ok:
                if (!country_code.equalsIgnoreCase("") && !country_name.equalsIgnoreCase("")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(FinalKeywordClass.CountryCode, country_code);
                    resultIntent.putExtra(FinalKeywordClass.CountryName, country_name);
                    resultIntent.putExtra(FinalKeywordClass.CountryId, "+" + country_id);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } else {
                    //toast
                    ScankBarMethod.showSnack(getCurrentFocus(), FinalKeywordClass.PLZSELECT_COUNTRY);
                }


                break;
        }
    }
}
