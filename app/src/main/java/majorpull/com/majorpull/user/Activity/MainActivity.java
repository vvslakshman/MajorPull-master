package majorpull.com.majorpull.user.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.HashMap;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.LogoutResponse;
import ResultResponse.Userdata;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.ActivityMainBinding;
import majorpull.com.majorpull.user.Fragment.SettingUserFragment;
import majorpull.com.majorpull.user.Fragment.UserProfileFragment;
import majorpull.com.majorpull.user.Fragment.HomeFragment;
import majorpull.com.majorpull.user.Fragment.ScheduledTrips_UserFragment;
import majorpull.com.majorpull.user.Fragment.YourTripFragment;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.CustomTypefaceSpan;
import majorpull.com.majorpull.util.FinalKeywordClass;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//public class MainActivity extends AppCompatActivity implements DrawerLocker, View.OnClickListener {
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener {

    public static DrawerLayout drawer;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private ActivityMainBinding mainBinding;
    Toolbar toolbar;
    private RelativeLayout rlmain;
    private ImageView ivprofile, ivping;
    private TextView username;
    private String firstName = "", lastName = "";
    RelativeLayout rl_home, rl_yourtrips, rl_scheduledtrips, rl_myprofile, rl_setting, rl_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        rlmain = (RelativeLayout) findViewById(R.id.rl_main);

        ImageView menu = (ImageView) toolbar.findViewById(R.id.iv_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        ivping = (ImageView) toolbar.findViewById(R.id.iv_ping);
        ivping.setOnClickListener(this);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


//        View Header = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_navigationdrawer_userprofile, null);


        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        View Header = navigationView.getHeaderView(0);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Userdata userdatapref = SavePrefrence.getInstance(MainActivity.this).getUserdetail();
        ivprofile = (ImageView) Header.findViewById(R.id.imageView);
        username = (TextView) Header.findViewById(R.id.usernametext);

        Userdata userdata = SavePrefrence.getInstance(MainActivity.this).getUserdetail();


        firstName = userdata.getFirstName().toString();
        lastName = userdata.getLastName().toString();
        username.setText(firstName + lastName);
        Log.v("UserName", firstName + lastName + "");

        Log.v("Prefrence_SaveData", userdata.toString());

        Glide.with(MainActivity.this).load(SavePrefrence.getInstance(MainActivity.this).getUserdetail().getProfilePicThumb()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivprofile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivprofile.setImageDrawable(circularBitmapDrawable);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            public void onDrawerOpened(View drawerview) {
                super.onDrawerOpened(drawerview);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                rlmain.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                //below line used to remove shadow of drawer
//                mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            }

        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);


        Bundle bundle = new Bundle();
//       if(CommonMethod.getPrefrence(MainActivity.this,"UserLogin").equalsIgnoreCase("FbLogin")){
//            String name=Intent.getIntent()
//        }
        if (getIntent().getBooleanExtra("start_ride_confirm_dialog", false)) {
            bundle.putBoolean("start_ride_confirm_dialog", getIntent().getBooleanExtra("start_ride_confirm_dialog", false));
        } else if (getIntent().getBooleanExtra("payment_success_dialog", false)) {
            bundle.putBoolean("payment_success_dialog", getIntent().getBooleanExtra("payment_success_dialog", false));

        }

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);


        fragmentManager.beginTransaction().replace(R.id.container_user, homeFragment).commit();
        drawer.closeDrawers();
    }


    @Override
    public void onBackPressed() {

        Fragment fragment = fragmentManager.findFragmentById(R.id.container_user);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof HomeFragment) {
            ExitDialog();
        } else {
            super.onBackPressed();
        }

    }


    //    LogoutDialogMethod
    private void ExitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("MajorPull");
        builder.setMessage("Are you sure want to Log out?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int paramInt) {


                if (IsOnLineMethod.isOnline(MainActivity.this)) {

                    call_LogoutApi();

                }



                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int paramInt) {

                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void call_LogoutApi() {


        ProgressDialog.showProgressDio(MainActivity.this);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL, SavePrefrence.getInstance(MainActivity.this).getUserToken()).
                create(ApiInterface.class);
        final Map maplogout = new HashMap();

        maplogout.put(ApiClass.getmApiClass().USER_ID, SavePrefrence.getInstance(MainActivity.this).getUserdetail().getUserId());
        maplogout.put(ApiClass.getmApiClass().DeviceType, FinalKeywordClass.DeviceType);
        maplogout.put(ApiClass.getmApiClass().DeviceID, FinalKeywordClass.DeviceID);
        Call<LogoutResponse> call = mApiInterface.logout(maplogout);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {


                LogoutResponse logoutResponse = response.body();
                if (response.isSuccessful()) {
                    ProgressDialog.hideProgressDio();
                    SavePrefrence.getInstance(MainActivity.this).removeUserDeatil(MainActivity.this);

                    startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
                    finish();
                    Log.v("Response:", response.body() + "");

                } else {

                    ScankBarMethod.showSnack(getCurrentFocus(), "Somthing Wronge" + response.toString());
                }

            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                ProgressDialog.hideProgressDio();

                Log.v("OnFailureMethod:", t + "");
            }
        });

    }


//    @Override
//    public void setDrawerEnabled(Boolean enabled) {
//        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
//                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
//        drawer.setDrawerLockMode(lockMode);
//        toggle.setDrawerIndicatorEnabled(enabled);
//    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

//            case R.id.rl_home:
//                HomeFragment homeFragment = new HomeFragment();
//                fragmentManager.beginTransaction().replace(R.id.container_user, homeFragment).addToBackStack(null).commit();
//                drawer.closeDrawers();
//
//                break;
//
//            case R.id.rl_yourtrips:
//                YourTripFragment yourTripFragment = new YourTripFragment();
//                fragmentManager.beginTransaction().replace(R.id.container_user, yourTripFragment).addToBackStack(null).commit();
//                CommonMethod.setPrefrence(MainActivity.this, "Majorpull_user", "Your_Trip");
//
//                drawer.closeDrawers();
//
//                break;
            case R.id.iv_ping:

                startActivity(new Intent(this, NotificationUserActivity.class));

                break;
//            case R.id.rl_scheduledtrips:
//
//                ScheduledTrips_UserFragment scheduledTrips_userFragment = new ScheduledTrips_UserFragment();
//                fragmentManager.beginTransaction().replace(R.id.container_user, scheduledTrips_userFragment).addToBackStack(null).commit();
//                CommonMethod.setPrefrence(MainActivity.this, "Majorpull_user", "Scheduled_Trip");
//                drawer.closeDrawers();
//
//                break;
//            case R.id.rl_myprofile:
//
//
//                UserProfileFragment userProfileFragment = new UserProfileFragment();
//                fragmentManager.beginTransaction().replace(R.id.container_user, userProfileFragment).addToBackStack(null).commit();
////                startActivity(new Intent(this, UserProfileFragment.class));
//                drawer.closeDrawers();
//
//                break;
//
//            case R.id.rl_settingg:
//
//                SettingUserFragment settingUserFragment = new SettingUserFragment();
//                fragmentManager.beginTransaction().replace(R.id.container_user, settingUserFragment).addToBackStack(null).commit();
//                drawer.closeDrawers();
//
//
////                startActivity(new Intent(this, SettingUserFragment.class));
////                drawer.closeDrawers();
//
//                break;
//
//            case R.id.rl_logout:
//                ExitDialog();
//                drawer.closeDrawers();
//                break;
        }
    }


//    NavigationDrawerMethod

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.container_user, homeFragment).addToBackStack(null).commit();
//            drawer.closeDrawers();

            // Handle the camera action
        } else if (id == R.id.nav_your_trip) {
//            applyFontToMenuItem(item);
            YourTripFragment yourTripFragment = new YourTripFragment();
            fragmentManager.beginTransaction().replace(R.id.container_user, yourTripFragment).addToBackStack(null).commit();
            CommonMethod.setPrefrence(MainActivity.this, "Majorpull_user", "Your_Trip");

//            drawer.closeDrawers();

        } else if (id == R.id.nav_scheduledttrips) {
            ScheduledTrips_UserFragment scheduledTrips_userFragment = new ScheduledTrips_UserFragment();
            fragmentManager.beginTransaction().replace(R.id.container_user, scheduledTrips_userFragment).addToBackStack(null).commit();
            CommonMethod.setPrefrence(MainActivity.this, "Majorpull_user", "Scheduled_Trip");
//            drawer.closeDrawers();
        } else if (id == R.id.nav_myprofile) {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.container_user, userProfileFragment).addToBackStack(null).commit();
//            drawer.closeDrawers();
        } else if (id == R.id.nav_settings) {
            SettingUserFragment settingUserFragment = new SettingUserFragment();
            fragmentManager.beginTransaction().replace(R.id.container_user, settingUserFragment).addToBackStack(null).commit();
//            drawer.closeDrawers();
        } else if (id == R.id.nav_logout) {
            ExitDialog();
//            drawer.closeDrawers();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/rubikregular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


}
