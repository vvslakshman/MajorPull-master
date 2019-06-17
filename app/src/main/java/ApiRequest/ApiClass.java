package ApiRequest;

/**
 * Created by user102 on 3/5/18.
 */

public class ApiClass {


    public static String BASE_URL = "http://demo2server.com/major_pull/api/";
    private static ApiClass mApiClass;
    public static final String AUTHORIZATION = "Authorization";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String DeviceType = "device_type";
    public static final String DeviceID = "device_id";
    public static final String FIRSTNAME = "first_name";
    public static final String LASTNAME = "last_name";
    public static final String COUNTRY_ID = "country_id";
    public static final String COUNTRY = "country_name";
    public static final String COUNTRYCODE = "country_code";
    public static final String PROFILE_PIC="profile_pic";

    public static final String COUNTRYNAME = "name";
    public static final String PHONE = "phone_number";
    public static final String SOCIAL_ID = "social_id";
    public static final String SOCIAL_TYPE = "social_type";
    public static final String CURRENT_PASSWORD = "currentPassword";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String CONFIRM_NEW_PASSWORD = "confirmNewPassword";


    public static ApiClass getmApiClass() {
        if (mApiClass == null) {
            mApiClass = new ApiClass();
        }
        return mApiClass;
    }

}
