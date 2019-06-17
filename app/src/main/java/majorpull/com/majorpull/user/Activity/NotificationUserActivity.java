package majorpull.com.majorpull.user.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityNotificationUserBinding;
import majorpull.com.majorpull.user.Adapter.NotificationUserAdapter;
import majorpull.com.majorpull.user.DataModel.NotificatonUserPojo;

/**
 * Created by user102 on 3/23/18.
 */

public class NotificationUserActivity extends AppCompatActivity implements View.OnClickListener {


    private NotificationUserAdapter adatper;

    private ActivityNotificationUserBinding notificationUserBinding;

    private ArrayList<NotificatonUserPojo> datalistMethod() {

        ArrayList<NotificatonUserPojo> arrayList = new ArrayList<>();
        String s = getResources().getString(R.string.title_str);
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));

        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));
        arrayList.add(new NotificatonUserPojo(R.drawable.logo_icon_withbg, getString(R.string.title_str), getString(R.string.description)));

        return arrayList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification_user);

        notificationUserBinding.recyclerNotificatoin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<NotificatonUserPojo> list = datalistMethod();
        adatper = new NotificationUserAdapter(this, list);
        notificationUserBinding.recyclerNotificatoin.setAdapter(adatper);
        notificationUserBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        notificationUserBinding.toolbar.ivMenu.setVisibility(View.GONE);
        notificationUserBinding.toolbar.ivPing.setVisibility(View.GONE);
        notificationUserBinding.tvShow.setOnClickListener(this);
        notificationUserBinding.toolbar.ivBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_show:

                ShowNotification(v);

                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void ShowNotification(View v) {

        if (notificationUserBinding.tvShow.isSelected()) {

            notificationUserBinding.recyclerNotificatoin.setVisibility(View.VISIBLE);
            v.setSelected(false);
//                    notificationUserBinding.tvShow.setSelected(false);
            Log.v("Selected", "Gone");
        } else {
            notificationUserBinding.recyclerNotificatoin.setVisibility(View.GONE);
//                    notificationUserBinding.tvShow.setSelected(true);
            v.setSelected(true);
            Log.v("Selected", "Visible");
        }

    }
}
