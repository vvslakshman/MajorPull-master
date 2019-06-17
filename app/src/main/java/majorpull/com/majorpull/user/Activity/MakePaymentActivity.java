package majorpull.com.majorpull.user.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityMakePaymentBinding;

/**
 * Created by user102 on 3/20/18.
 */

public class MakePaymentActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityMakePaymentBinding makePaymentBinding;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makePaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_make_payment);
        makePaymentBinding.tvSubmit.setOnClickListener(this);
        makePaymentBinding.toolbar.ivMenu.setVisibility(View.GONE);
        makePaymentBinding.toolbar.ivPing.setVisibility(View.GONE);
//        makePaymentBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        makePaymentBinding.toolbar.ivBack.setOnClickListener(this);
        makePaymentBinding.etExpriyDate.setOnClickListener(this);
        makePaymentBinding.llCash.setOnClickListener(this);
        makePaymentBinding.llDebitcard.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_submit:

//                startActivity(new Intent(getApplicationContext(), ConfirmRidingActivity.class));

                Intent intent = new Intent(MakePaymentActivity.this, MainActivity.class);
                intent.putExtra("payment_success_dialog", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
            case R.id.et_expriy_date:
                datePickerDailog();
                break;


            case R.id.ll_cash:

                if (makePaymentBinding.llCash.isSelected()) {
                    makePaymentBinding.ivDebitCraditCart.setSelected(true);
                    makePaymentBinding.llDebitcard.setSelected(true);
                    makePaymentBinding.llCash.setSelected(false);
                    makePaymentBinding.ivCash.setSelected(false);
                    makePaymentBinding.llBottemdebitcard.setVisibility(View.VISIBLE);
                    makePaymentBinding.viewDebitcard.setVisibility(View.GONE);

                } else {

                    makePaymentBinding.llCash.setSelected(true);
                    makePaymentBinding.ivCash.setSelected(true);
                    makePaymentBinding.ivDebitCraditCart.setSelected(false);
                    makePaymentBinding.llDebitcard.setSelected(false);
                    makePaymentBinding.llBottemdebitcard.setVisibility(View.GONE);
                    makePaymentBinding.viewDebitcard.setVisibility(View.VISIBLE);


                }

                break;

            case R.id.ll_debitcard:
                if (makePaymentBinding.llCash.isSelected()) {
                    makePaymentBinding.ivDebitCraditCart.setSelected(true);
                    makePaymentBinding.llDebitcard.setSelected(true);
                    makePaymentBinding.llCash.setSelected(false);
                    makePaymentBinding.ivCash.setSelected(false);
                    makePaymentBinding.llBottemdebitcard.setVisibility(View.VISIBLE);
                    makePaymentBinding.viewDebitcard.setVisibility(View.GONE);


                } else {
                    makePaymentBinding.llCash.setSelected(true);
                    makePaymentBinding.ivCash.setSelected(true);
                    makePaymentBinding.ivDebitCraditCart.setSelected(false);
                    makePaymentBinding.llDebitcard.setSelected(false);
                    makePaymentBinding.llBottemdebitcard.setVisibility(View.GONE);
                    makePaymentBinding.viewDebitcard.setVisibility(View.VISIBLE);

                }
                break;
        }
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

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        makePaymentBinding.etExpriyDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


    //    @Override
//    public void onBackPressed() {
//        Intent i = new Intent();
//        i.putExtra("type", "yes");
//        this.setResult(888, i);
//        this.finish();
//    }
//


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888) {
            Intent i = new Intent();
            i.putExtra("type", "yes");
            this.setResult(888, i);
            this.finish();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
