package com.gokings.databasee;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("otp_varification.php/")
    Call<ResponseBody>send_otp(
            @Field("name") String name,
            @Field("phone") String phone);


    @FormUrlEncoded
    @POST("registration.php/")
    Call<ResponseBody>register(
            @Field("name") String name,
            @Field("phone") String phone);




    @FormUrlEncoded
    @POST("latlong.php/")
    Call<ResponseBody>SendLatlong(
            @Field("latitude") String lat,
            @Field("longitude") String lng);

}


