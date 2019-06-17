package majorpull.com.majorpull.user.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityProductDetailLayoutBinding;


/**
 * Created by user102 on 3/20/18.
 */

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityProductDetailLayoutBinding productDetailBinding;
    private Calendar mycalendra;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String materialtype, weight, date, time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail_layout);

        mycalendra = Calendar.getInstance();
        productDetailBinding.tvSubmit.setOnClickListener(this);

        productDetailBinding.etDob.setOnClickListener(this);
        productDetailBinding.etTime.setOnClickListener(this);
        productDetailBinding.toolbar.ivMenu.setVisibility(View.GONE);
        productDetailBinding.toolbar.ivPing.setVisibility(View.GONE);
        productDetailBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        productDetailBinding.toolbar.ivBack.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_dob:

                datePickerDailog();

                break;

            case R.id.et_time:
                TimePickerDailog();
                break;

            case R.id.iv_back:
                onBackPressed();
//                Intent i = new Intent();
//                i.putExtra("type", "false");
//                setResult(888, i);
//                this.finish();

                break;

            case R.id.tv_submit:

                productdetailValidation();

                startActivity(new Intent(getApplicationContext(), EstimatedPriceActivity.class));
                break;
        }
    }

    private void productdetailValidation() {

        materialtype = productDetailBinding.etMaterialtype.getText().toString();
        weight = productDetailBinding.etWeightkg.getText().toString();
        date = productDetailBinding.etDob.getText().toString();
        time = productDetailBinding.etTime.getText().toString();
        productDetailBinding.etMassage.getText().toString();


    }

    //    TimePickerMethod
    private void TimePickerDailog() {


        // Get Current Time
        final Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);


//        Calendar calendar = Calendar.getInstance();
//        calendar.set(0, 0, 0, mHour, mMinute, 0);

        long timeInMillis = calendar.getTimeInMillis();
        final java.text.DateFormat dateFormatter = new SimpleDateFormat("hh : mm : a");
        final Date date = new Date();
        date.setTime(timeInMillis);
        productDetailBinding.etTime.setText(dateFormatter.format(date));


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

//                        productDetailBinding.etTime.setText(hourOfDay + ":" + minute);
                        productDetailBinding.etTime.setText(dateFormatter.format(date));
                        Log.v("Time Show", dateFormatter.format(date) + "");
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }


    //    DatePickerMethod
    private void datePickerDailog() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    //In which you need put here


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth);
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");


                        productDetailBinding.etDob.setText(sdf.format(date));
                        Log.v("Date Show", sdf.format(date) + "");

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == 888) {
//            if (data.getStringExtra("type").equals("yes")) ;
//            finish();
//        } else
//            super.onActivityResult(requestCode, resultCode, data);

    }
}
