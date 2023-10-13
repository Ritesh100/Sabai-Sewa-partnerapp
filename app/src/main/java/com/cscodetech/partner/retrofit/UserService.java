package com.cscodetech.partner.retrofit;


import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {

    @POST(APIClient.APPEND_URL + "p_reg_user.php")
    Call<JsonObject> createUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_user_login.php")
    Call<JsonObject> loginUser(@Body RequestBody requestBody);


    @POST(APIClient.APPEND_URL + "p_country_code.php")
    Call<JsonObject> getCodelist(@Body RequestBody requestBody);


    @POST(APIClient.APPEND_URL + "new_order.php")
    Call<JsonObject> getHome(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "update_category.php")
    Call<JsonObject> updateCategory(@Body RequestBody requestBody);


    @POST(APIClient.APPEND_URL + "p_cat_list.php")
    Call<JsonObject> getCategory(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_order_status_change.php")
    Call<JsonObject> statusChange(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_credit_list.php")
    Call<JsonObject> creditList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_paymentgateway.php")
    Call<JsonObject> paymentlist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_credit_up.php")
    Call<JsonObject> pCreditUp(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "p_getdata.php")
    Call<JsonObject> getCredit(@Body RequestBody requestBody);



    @POST(APIClient.APPEND_URL + "p_mobile_check.php")
    Call<JsonObject> getMobileCheck(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "p_payoutdata.php")
    Call<JsonObject> getPayoutData(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "p_payout_list.php")
    Call<JsonObject> getPayoutlist(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "request_payout.php")
    Call<JsonObject> requestPayout(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "p_forget_password.php")
    Call<JsonObject> getForgot(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "p_profile.php")
    @Multipart
    Call<JsonObject> uploadMultiFile(@Part("uid") RequestBody uid, @Part("name") RequestBody name, @Part("password") RequestBody password, @Part("address") RequestBody address, @Part("bio") RequestBody bio, @Part("size") RequestBody size, @Part List<MultipartBody.Part> parts);

    @POST(APIClient.APPEND_URL + "list_addon.php")
    Call<JsonObject> listaddon(@Body RequestBody requestBody);
}
