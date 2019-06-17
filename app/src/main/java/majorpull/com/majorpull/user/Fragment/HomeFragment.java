package majorpull.com.majorpull.user.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FragmentHomeLayoutBinding;
import majorpull.com.majorpull.user.Activity.MakePaymentActivity;
import majorpull.com.majorpull.user.Activity.ProductDetailsActivity;
import majorpull.com.majorpull.user.Activity.RatingReviewUserActivity;

/**
 * Created by user102 on 3/20/18.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {


    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 10;
    private View view;
    private Context context;
    private FragmentHomeLayoutBinding binding;
    private GoogleMap map;
    private int RequestPermissionCode = 1;
    private Location location;
    private LocationManager locationManager;
    private String bestProvider;
    private String TAG = "HomeFragment";
    private String provider;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLocationPermission();


        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home_layout, container, false);

        context = container.getContext();
        view = binding.getRoot();


        binding.tvSchedulRide.setOnClickListener(this);
        binding.tvBokkmyRide.setOnClickListener(this);
        binding.tvRating.setOnClickListener(this);
        FragmentManager fragmentManager = getChildFragmentManager();

        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermission();

        if (getArguments() != null)
            if (getArguments().getBoolean("start_ride_confirm_dialog", false)) {
                thankyouDialog();
            } else if (getArguments().getBoolean("payment_success_dialog", false)) {
                submitDialog();
            }

        return view;
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }

    }

    private void getLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                Manifest.permission.CAMERA)) {

            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_schedul_ride:
                startActivity(new Intent(context, ProductDetailsActivity.class));
                break;
            case R.id.tv_bokkmy_ride:
                startActivity(new Intent(context, ProductDetailsActivity.class));
                break;
            case R.id.tv_rating:
                startActivity(new Intent(context, RatingReviewUserActivity.class));

                break;

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        map = googleMap;

        Drawable circleDrawable = getResources().getDrawable(R.drawable.marker_icon);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        LatLng india = new LatLng(26.8945181, 75.7366531);
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
//
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                .build();
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (map != null)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.294966, 48.020935), 12.0f));

        googleMap.addMarker(new MarkerOptions().position(india).icon(markerIcon)
                .title("The Nine Hertz India"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(india));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.


//                    showAlert(getString(R.string.error), getString(R.string.message));
                }
            }
            Log.e("RequestCode=>", requestCode + "");
        }

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void thankyouDialog() {
        final Dialog dailog = new Dialog(getActivity());
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        dailog.setContentView(R.layout.dailog_thanks_booking_layout);
        dailog.show();

        dailog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillAmmountDialog();
                dailog.dismiss();
            }
        });

    }

    private void BillAmmountDialog() {


        final Dialog dailog = new Dialog(getActivity());
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));

        dailog.setContentView(R.layout.dailog_payment_sucessfull);
        dailog.show();


        TextView tvmassage = dailog.findViewById(R.id.tv_massage);
        tvmassage.setText(
                getResources().getString(R.string.yourtrip_suceess));

        TextView tvRatingreview = dailog.findViewById(R.id.tv_ratingreview);
        tvRatingreview.setText(getResources().getString(R.string.make_payment));
        tvRatingreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), MakePaymentActivity.class), 888);
//                finish();

                dailog.dismiss();
            }
        });


    }

    private void submitDialog() {


        final Dialog dailog = new Dialog(getActivity());
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        dailog.setContentView(R.layout.dailog_payment_sucessfull);
        dailog.show();

        ImageView ivclose = dailog.findViewById(R.id.iv_close);

        ivclose.setVisibility(View.VISIBLE);
        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
            }
        });
        dailog.findViewById(R.id.iv_close).setVisibility(View.VISIBLE);


        dailog.findViewById(R.id.tv_total_amount).setVisibility(View.GONE);

        dailog.findViewById(R.id.tv_ratingreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getActivity(), RatingReviewUserActivity.class), 888);
                dailog.dismiss();
            }
        });


    }


}
