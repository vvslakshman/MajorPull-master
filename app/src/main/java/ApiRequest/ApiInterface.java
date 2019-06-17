package ApiRequest;

import java.util.Map;

import ResultResponse.CheckUserResponse;
import ResultResponse.CountryResponce;
import ResultResponse.ForgotResponse;
import ResultResponse.GetProfileResponse;
import ResultResponse.LogoutResponse;
import ResultResponse.OtpResponse;
import ResultResponse.SignINResponce;
import ResultResponse.SignUpResponse;
import ResultResponse.SplashResponce;
import ResultResponse.UpdateProfileResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {


  /*  @POST("auth/get-api-key")
    @FormUrlEncoded
    Call<Accesstoken> getaccessToken(@FieldMap Map<String, String> parmas);

    @POST("getVerifyOtp")
     @Multipart
     Call<LoginResponse> getVerifyOtpApi(@PartMap Map<String, RequestBody> parmas);

    @POST("getVerifyOtp")
    @Multipart
    Call<LoginResponse> getVerifyOtpApi(@PartMap Map<String, RequestBody> parmas, @Part MultipartBody.Part profile_picture);
*/

    @POST("login")
    @FormUrlEncoded
    Call<SignINResponce> getSignin(@FieldMap Map<String, RequestBody> parmas);


    @POST("auth/get-api-key")
    @FormUrlEncoded
    Call<SplashResponce> getsplash(@FieldMap Map<String, RequestBody> parmas);

    @GET("get-countries")
    Call<CountryResponce> getCountryList();

    @POST("verify-for-signup-and-get-otp")
    @FormUrlEncoded
    Call<OtpResponse> getOtp(@FieldMap Map<String, RequestBody> parmas);


    @POST("sign-up")
    @FormUrlEncoded
    Call<SignUpResponse> getSginup(@FieldMap Map<String, RequestBody> parmas);

    @POST("get-user-profile")
    @FormUrlEncoded
    Call<GetProfileResponse> getuserProfile(@FieldMap Map<String, RequestBody> parmas);


    @POST("update-profile")
    @Multipart
    Call<UpdateProfileResponse> updateprofile(@PartMap Map<String, RequestBody> parmas, @Part MultipartBody.Part profile_picture);


    @POST("change-password")
    @FormUrlEncoded
    Call<String> changepassword(@FieldMap Map<String, RequestBody> parmas);


//    @POST("sign-up")
//    @FormUrlEncoded
//    Call<String> getSginup(@FieldMap Map<String, RequestBody> parmas);


    @GET("getProfile/{user_id}")
    Call<SignINResponce> getProfile(@Path("user_id") String userid);


    @POST("logout")
    @FormUrlEncoded
    Call<LogoutResponse> logout(@FieldMap Map<String, Object> parmas);


    @POST("update-password-by-phone-number")
    @FormUrlEncoded
    Call<ForgotResponse> resetpassword(@FieldMap Map<String, Object> parmas);


    @POST("checkuser")
    @FormUrlEncoded
    Call<CheckUserResponse> chekc_user(@FieldMap Map<String, Object> parmas);



   /* @POST("getLogin")
    @FormUrlEncoded
    Call<LoginResponse> getLogin(@FieldMap Map<String, String> parmas);




    @POST("updateProfile")
    @Multipart
    Call<ProfileResponse> updateProfile(@PartMap Map<String, RequestBody> parmas, @Part MultipartBody.Part profile_picture);


    @POST("updateProfile")
    @Multipart
    Call<ProfileResponse> updateProfile(@PartMap Map<String, RequestBody> parmas);


    @POST("forgotpassword")
    @FormUrlEncoded
    Call<Basic_Response> forgot(@FieldMap Map<String, Object> parmas);

    @POST("changePassword")
    @FormUrlEncoded
    Call<Basic_Response> changePassword(@FieldMap Map<String, Object> parmas);

    @POST("getCategoryList")
    @FormUrlEncoded
    Call<CategoryList> getCategory(@FieldMap Map<String, Object> parmas);

    @POST("getMenuList")
    @FormUrlEncoded
    Call<MenuList> getMenuList(@FieldMap Map<String, Object> parmas);

    @POST("getMenuList")
    @FormUrlEncoded
    Call<OfferThemeResponse> getOfferTheme(@FieldMap Map<String, Object> parmas);

    @POST("getSubmenuLIst")
    @FormUrlEncoded
    Call<SubMenuResponse> getSubmenuLIst(@FieldMap Map<String, Object> parmas);



    @POST("getAppetizer")
    @FormUrlEncoded
    Call<AppetizerResponse> getAppetizerList(@FieldMap Map<String, Object> parmas);


    @POST("getTableList")
    @FormUrlEncoded
    Call<TableListResponse> getTableList(@FieldMap Map<String, Object> parmas);


    @POST("tableStatus")
    @FormUrlEncoded
    Call<Basic_Response> tableStatus(@FieldMap Map<String, Object> parmas);

    @POST("getdiscounts")
    @FormUrlEncoded
    Call<CalculationDiscount> getRestaurantDiscountList(@FieldMap Map<String, Object> parmas);

    @POST("getAppetizerdetail")
    @FormUrlEncoded
    Call<AppetizerdetailResponse> getAppetizerdetail(@FieldMap Map<String, Object> parmas);

    @POST("getSubmenuLIstInAlcohol")
    @FormUrlEncoded
    Call<SubMenuResponse> getSubmenuLIstInAlcohol(@FieldMap Map<String, Object> parmas);

    @POST("getCheckout")
    @FormUrlEncoded
    Call<Basic_Response> getCheckout_Detauil(@FieldMap Map<String, Object> parmas);

    @POST("getMyOrders")
    @FormUrlEncoded
    Call<MyOrderResponse> getMyOrders(@FieldMap Map<String, Object> parmas);

    @POST("getAllWaiterRelatedToOrder")
    @FormUrlEncoded

    Call<WaiterListResponse> getAllWaiterRelatedToOrder(@FieldMap Map<String, Object> parmas);

    @POST("addWaiterTip")
    @FormUrlEncoded
    Call<Basic_Response> addWaiterTip(@FieldMap Map<String, Object> parmas);


    @GET("getLoyaltyPoint/{user_id}")
    Call<LoyaltyPointsResponse> getLoyaltyPoint(@Path("user_id") String userid);


    @POST("getRatingTypes")
    @FormUrlEncoded
    Call<FeedbackResponse> getRatingTypes(@FieldMap Map<String, Object> parmas);


    @POST("setRatingByUser")
    @FormUrlEncoded
    Call<Basic_Response> setRatingByUser(@FieldMap Map<String, Object> parmas);


    @POST("getNotification")
    @FormUrlEncoded
    Call<NotificationResponse> getNotification(@FieldMap Map<String, Object> parmas);

    @POST("setNotificationSeen")
    @FormUrlEncoded
    Call<Basic_Response> setNotificationSeen(@FieldMap Map<String, Object> parmas);

    @POST("LipaNaMpesaOnlineBrew")
    @FormUrlEncoded
    Call<MpesaResponse> mpessapi(@FieldMap Map<String, Object> parmas);

    *//*@GET("searchItem")
    Call<String> getSearchitem(@Query("user_id") String userid, @Query("search_text") String search_text);
*//*

    @POST("searchItem")
    @FormUrlEncoded
    Call<SearchResponse> getSearchitem(@FieldMap Map<String, Object> parmas);*/

    /*@GET("getProfile")
    Call<String> getGulfProfile(@QueryMap Map <String, Object>parmas);



*/


}
