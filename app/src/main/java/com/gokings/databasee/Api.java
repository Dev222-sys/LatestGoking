package com.gokings.databasee;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("otp_varification.php/")
    Call<ResponseBody> send_otp(
            @Field("name") String name,
            @Field("phone") String phone);


    @FormUrlEncoded
    @POST("registration.php/")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("phone") String phone);


    @FormUrlEncoded
    @POST("address_reg.php/")
    Call<ResponseBody> SendLatlong(

            @Field("id") String id,
            @Field("location_type") String location_type,
            @Field("lat") String lat,
            @Field("long") String longt);


    @FormUrlEncoded
    @POST("get_user_details.php")
    Call<ResponseBody>get_edit_profil(
            @Field("id") String id

    );

    @FormUrlEncoded
    @POST("edit_user_details.php")
    Call<ResponseBody>update_profile(
            @Field("id") String id,
            @Field("full_name") String full_name,
            @Field("school_name") String school_name,
            @Field("school_categories") String school_categories

    );




    @FormUrlEncoded
    @POST("details_registration.php/")
    Call<ResponseBody> send_school_deatils(

            @Field("id") String id,
            @Field("school name") String school_name,
            @Field("school categories") String school_categories
    );


    @FormUrlEncoded
    @POST("online_person.php/")
    Call<ResponseBody> sendradius(

            @Field("id") String id,
            @Field("radius") String radius,
            @Field("school name") String school_name,
            @Field("school categories") String school_categories,
            @Field("location") String location
    );

/*
    @FormUrlEncoded
    @POST("online_person.php/")
    Call<ResponseBody> sendradius(

            @Field("id") String id,
            @Field("radius") String radius,
            @Field("school name") String school_name,
            @Field("school categories") String school_categories
    );
*/

   /* @FormUrlEncoded
    @POST("online_person.php/")
    Call<ResponseBody> online_person(
            @Field("id") String id
    );
*/
}


