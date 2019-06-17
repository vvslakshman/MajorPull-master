package majorpull.com.majorpull.user.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ApiRequest.ApiClass;
import ApiRequest.ApiInterface;
import ApiRequest.RequestClient;
import ResultResponse.GetProfileResponse;
import ResultResponse.UpdateProfileResponse;
import majorpull.com.majorpull.Manifest;
import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FragmentUserprofileLayoutBinding;
import majorpull.com.majorpull.user.Activity.OTPVerificationActivitiy;
import majorpull.com.majorpull.util.CommonMethod;
import majorpull.com.majorpull.util.Country_CodeListActivity;
import majorpull.com.majorpull.util.DebugLog;
import majorpull.com.majorpull.util.EmailValidation;
import majorpull.com.majorpull.util.FinalKeywordClass;
import majorpull.com.majorpull.util.IsOnLineMethod;
import majorpull.com.majorpull.util.ProgressDialog;
import majorpull.com.majorpull.util.SavePrefrence;
import majorpull.com.majorpull.util.ScankBarMethod;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user102 on 3/22/18.
 */

public class UserProfileFragment extends Fragment implements View.OnClickListener {


    private static final int CAM_REQUEST = 1, RESULT_LOAD_IMAGE = 2;
    private View view;
    private Context mcontext;
    private FragmentUserprofileLayoutBinding bindingprofile;
    private String firstname, lastname, mobilenumber, update_contact, email, country, countryId, plush = "+", countrycode = "", strImagepath = "";

    private SavePrefrence savePrefrence;
    private String userId;
    private Bitmap dpbtmp = null;
    private File file = null;
    private Uri mImageUri = null;
    private static final int RequestPermissionCode = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        bindingprofile = DataBindingUtil.inflate(inflater, R.layout.fragment_userprofile_layout, container, false);

        mcontext = container.getContext();

        view = bindingprofile.getRoot();


        bindingprofile.etCountrycode.setOnClickListener(this);
        bindingprofile.etCountry.setOnClickListener(this);
//        bindingprofile.ivProfile.setOnClickListener(this);
        bindingprofile.ivEdit.setOnClickListener(this);
        bindingprofile.tvSubmit.setOnClickListener(this);


//        bindingprofile.ivProfile.setImageResource(R.drawable.user_immg);


//        call_GetProfileApi
        if (IsOnLineMethod.isOnline(getActivity())) {
            call_Getprofile_Api();
        } else {
            ScankBarMethod.showSnack(getActivity().getCurrentFocus(), getResources().getString(R.string.check_internet_connection));
        }


        EnableRunTimePermission();

        return view;
    }

    private void call_Getprofile_Api() {

        ProgressDialog.showProgressDio(mcontext);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL,
                SavePrefrence.getInstance(mcontext).getUserToken()).
                create(ApiInterface.class);
        final Map mapsignup = new HashMap();
        userId = SavePrefrence.getInstance(mcontext).getUserdetail().getUserId().toString();
        mapsignup.put(ApiClass.getmApiClass().USER_ID, userId);


        Call<GetProfileResponse> call = mApiInterface.getuserProfile(mapsignup);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

                if (response.isSuccessful()) {
                    GetProfileResponse getProfileResponse = response.body();

                    Log.v("Response:", response + "");
                    if (getProfileResponse != null) {
//                        SaveDataInSavePrefrence
                        SavePrefrence.getInstance(mcontext).saveUserdetail(getProfileResponse.getData());


                        Glide.with(mcontext).load(getProfileResponse.getData().getProfilePicThumb()).asBitmap().centerCrop().into(new BitmapImageViewTarget(bindingprofile.ivProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mcontext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                bindingprofile.ivProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        bindingprofile.etFname.setText(getProfileResponse.getData().getFirstName());
                        bindingprofile.etLname.setText(getProfileResponse.getData().getLastName());
                        bindingprofile.etMobilenumber.setText(getProfileResponse.getData().getPhoneNumber());
                        bindingprofile.etEmail.setText(getProfileResponse.getData().getEmail());
                        countrycode = getProfileResponse.getData().getCountryId().toString();
                        bindingprofile.etCountry.setText(getProfileResponse.getData().getCountryName());
                        ProgressDialog.hideProgressDio();
                    }

                }

            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                ProgressDialog.hideProgressDio();

                Log.v("OnFailure", t + "");

            }
        });


    }

    private void profile_validation() {

        firstname = bindingprofile.etFname.getText().toString();
        lastname = bindingprofile.etLname.getText().toString();
        mobilenumber = bindingprofile.etMobilenumber.getText().toString();
        email = bindingprofile.etEmail.getText().toString();
        country = bindingprofile.etCountry.getText().toString();
        update_contact = SavePrefrence.getInstance(mcontext).getUserdetail().getPhoneNumber().toString();

        if (!(bindingprofile.etFname.length() > 0)) {
            bindingprofile.etFname.setError(getResources().getString(R.string.enter_firstname));
        } else if (!(bindingprofile.etLname.length() > 0)) {
            bindingprofile.etLname.setError(getResources().getString(R.string.enter_last_name));
        } else if (bindingprofile.etMobilenumber.length() < 1) {
            bindingprofile.etMobilenumber.setError(getResources().getString(R.string.enter_contact));
        } else if (bindingprofile.etMobilenumber.length() < 7) {
            bindingprofile.etMobilenumber.setError(getResources().getString(R.string.mobileno_mini_length_error));
        } else if (bindingprofile.etMobilenumber.length() > 15) {
            bindingprofile.etMobilenumber.setError(getResources().getString(R.string.mobileno_maxlength_error));
        } else if (!(bindingprofile.etEmail.length() > 0)) {
            bindingprofile.etEmail.setError(getResources().getString(R.string.enter_email));
        } else if (!EmailValidation.checkEmail(email)) {
            bindingprofile.etEmail.setError(getResources().getString(R.string.invalid_email));
        } else if (!(bindingprofile.etCountry.length() > 0)) {
            bindingprofile.etCountry.setError(getResources().getString(R.string.enter_country));
        } else if (!(update_contact.equalsIgnoreCase(bindingprofile.etMobilenumber.getText().toString()))) {

//            Call to Verificationactivity
            firstname = bindingprofile.etFname.getText().toString();
            lastname = bindingprofile.etLname.getText().toString();
            mobilenumber = bindingprofile.etMobilenumber.getText().toString();
            email = bindingprofile.etEmail.getText().toString();
            country = bindingprofile.etCountry.getText().toString();
            countrycode = bindingprofile.etCountrycode.getText().toString();


            CommonMethod.setPrefrence(mcontext, "ProfileFragment", "UpdateProfile");
            Intent intent = new Intent(mcontext, OTPVerificationActivitiy.class);
            intent.putExtra(FinalKeywordClass.COMINGFROM, "profile");

            intent.putExtra(FinalKeywordClass.FNAME, firstname);
            intent.putExtra(FinalKeywordClass.LNAME, lastname);
            intent.putExtra(FinalKeywordClass.EMAIL, email);
            intent.putExtra(FinalKeywordClass.CONTACT, mobilenumber);
            intent.putExtra(FinalKeywordClass.CountryId, countryId);
//             if(file!=null)
//            intent.putExtra(FinalKeywordClass.PROFILE_LINK, file.getAbsolutePath());
            startActivity(intent);


        }

//        Valid All the Fields or Containt
        else {

            firstname = bindingprofile.etFname.getText().toString();
            lastname = bindingprofile.etLname.getText().toString();
            mobilenumber = bindingprofile.etMobilenumber.getText().toString();
            email = bindingprofile.etEmail.getText().toString();
            country = bindingprofile.etCountry.getText().toString();

//            ScankBarMethod.showSnack(view, getResources().getString(R.string.profile_update));

            if (IsOnLineMethod.isOnline(mcontext)) {

                call_updateProfile();
            } else {
                ScankBarMethod.showSnack(getActivity().getCurrentFocus(), getResources().getString(R.string.check_internet_connection));
            }


        }


    }

    private void call_updateProfile() {
        ProgressDialog.showProgressDio(mcontext);
        final ApiInterface mApiInterface = RequestClient.getClient(ApiClass.BASE_URL,
                SavePrefrence.getInstance(mcontext).getUserToken()).
                create(ApiInterface.class);
        final Map mapsignup = new HashMap();
        userId = SavePrefrence.getInstance(mcontext).getUserdetail().getUserId().toString();

        mapsignup.put(ApiClass.getmApiClass().USER_ID, RequestBody.create(MediaType.parse("multipart/form-data"), SavePrefrence.getInstance(mcontext).getUserdetail().getUserId().toString()));
        mapsignup.put(ApiClass.getmApiClass().FIRSTNAME, RequestBody.create(MediaType.parse("multipart/form-data"), bindingprofile.etFname.getText().toString()));
        mapsignup.put(ApiClass.getmApiClass().LASTNAME, RequestBody.create(MediaType.parse("multipart/form-data"), bindingprofile.etLname.getText().toString()));
        mapsignup.put(ApiClass.getmApiClass().EMAIL, RequestBody.create(MediaType.parse("multipart/form-data"), bindingprofile.etEmail.getText().toString()));
        mapsignup.put(ApiClass.getmApiClass().PHONE, RequestBody.create(MediaType.parse("multipart/form-data"), bindingprofile.etMobilenumber.getText().toString()));
        mapsignup.put(ApiClass.getmApiClass().COUNTRY, RequestBody.create(MediaType.parse("multipart/form-data"), bindingprofile.etCountry.getText().toString()));
        mapsignup.put(ApiClass.getmApiClass().COUNTRY_ID, RequestBody.create(MediaType.parse("multipart/form-data"), SavePrefrence.getInstance(mcontext).getUserdetail().getCountryId().toString()));

        MultipartBody.Part body = null;


        if (file != null && file.exists()) {
            Log.d("imagepath ", file.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData(ApiClass.getmApiClass().PROFILE_PIC, file.getName(), requestFile);

        }


        Call<UpdateProfileResponse> call = mApiInterface.updateprofile(mapsignup, body);

        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {


                if (response.isSuccessful()) {

                    UpdateProfileResponse updateProfileResponse = response.body();
                    Log.v("Response:", response + "");
                    Log.v("UpdateProfileResponse:", updateProfileResponse + "");

                    SavePrefrence.getInstance(mcontext).getUserdetail();

                    getActivity().onBackPressed();
                    ProgressDialog.hideProgressDio();


                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {

                ProgressDialog.hideProgressDio();

                DebugLog.log(1, "Response Error:", t + "");
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:


                insetImageDailog();

                //   openDialogForEditImage();
                break;

            case R.id.tv_submit:
                profile_validation();
                break;

            case R.id.et_countrycode:
                startActivityForResult(new Intent(mcontext, Country_CodeListActivity.class), 105);
                break;
            case R.id.et_country:
                startActivityForResult(new Intent(mcontext, Country_CodeListActivity.class), 105);
                break;

        }
    }


    private void EnableRunTimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                android.Manifest.permission.CAMERA)) {

            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }

    }

    private void insetImageDailog() {
        final Dialog onedialog = new Dialog(mcontext);
        onedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        onedialog.setContentView(R.layout.camera_open_dialog);
        onedialog.show();

        TextView tv_takeimg = onedialog.findViewById(R.id.tv_takeimg);
        TextView tv_cameraroll = onedialog.findViewById(R.id.tv_takecameraroll);
        TextView tv_cancel = onedialog.findViewById(R.id.tv_cancel);


        tv_takeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeCamera();
                onedialog.dismiss();
            }
        });

        tv_cameraroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeGallery();
                onedialog.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onedialog.dismiss();
            }
        });


    }

    private void activeGallery() {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent1, "Select File"), RESULT_LOAD_IMAGE);

    }


    private void activeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAM_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAM_REQUEST) {
                if (resultCode == RESULT_OK && data != null) {
                    dpbtmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bindingprofile.ivProfile.setImageBitmap(dpbtmp);

                    Uri uri = getImageUri(dpbtmp);
                    file = new File(uri.getPath());

                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                onSelectFromGalleryResult(data);

            } else if (requestCode == 105) {
                if (resultCode == RESULT_OK) {
                    countrycode = data.getExtras().getString(FinalKeywordClass.CountryCode);
                    country = data.getExtras().getString(FinalKeywordClass.CountryName);
                    countryId = data.getExtras().getString(FinalKeywordClass.CountryId);
                    bindingprofile.etCountrycode.setText(plush + countrycode);
                    bindingprofile.etCountry.setText(country);

                }
            } else if (requestCode == 101) {
                if (resultCode == RESULT_OK && data != null) {
                    dpbtmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bindingprofile.ivProfile.setImageBitmap(dpbtmp);

                    Uri uri = getImageUri(dpbtmp);
                    file = new File(uri.getPath());

                }
            } else if (requestCode == 100) {
                onSelectFromGalleryResult(data);
            }

        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e + "Something went wrong", Toast.LENGTH_LONG).show();

            DebugLog.log(1, "SomthingWrong", e + "");
        }


    }

    private void onSelectFromGalleryResult(Intent data) {
        //Bitmap bm=null;

        if (data != null) {
            try {
                dpbtmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri uri = getImageUri(dpbtmp);
                file = new File(uri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bindingprofile.ivProfile.setImageBitmap(dpbtmp);


    }


    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String pathh = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(pathh);
    }


    private void openDialogForEditImage() {
        final Dialog dialogMapMain = new Dialog(getActivity());
        //dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMapMain.setContentView(R.layout.camera_open_dialog);
        dialogMapMain.getWindow().setGravity(Gravity.CENTER);
        dialogMapMain.setCancelable(true);
        dialogMapMain.getWindow().setDimAmount(0.50f);
        dialogMapMain.show();

        dialogMapMain.findViewById(R.id.tv_takeimg).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageUri = getOutputMediaFileUri();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        getActivity().startActivityForResult(intent, 101);
                        dialogMapMain.dismiss();
                    }
                });

        dialogMapMain.findViewById(R.id.tv_takecameraroll).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialogMapMain.dismiss();
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(photoPickerIntent, 100);

                    }
                });

        dialogMapMain.findViewById(R.id.tv_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogMapMain.dismiss();
                    }
                });
    }


    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Majorpull");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Majorpull", "Oops! Failed create " + "Majorpull" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
