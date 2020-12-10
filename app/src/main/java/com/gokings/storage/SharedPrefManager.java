package com.gokings.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager minst;
    private static Context mct;
    private static final String LATITUDE="LATITUDE";
    private static final String LONGITUDE="LONGITUDE";
    private static final String SHARD_PERFNAME="myshardperf624";
    private static final String KEY_ID="id";

    private static final String KEY_SID="sid";

    private static final String KEY_TripID="tid";

    private static final String Device="divce";
    private static final String KEY_PICTURE="picture";
    private static final String KEY_USERNAME="name";
    private static final String KEY_BOOKING_ID="booking_id";
    private static final String KEY_USER_ID="user_id";
    private static final String KEY_EMAIL="email";
    private static final String NAME="first_name";
    private static final String KEY_LAST_NAME="last_name";
    private static final String KEY_MOBILE="mobile";

    private static final String ADDHOME="setAddHome";




    private SharedPrefManager(Context context){
        mct=context;
    }
    public static synchronized SharedPrefManager getInstans(Context context){
        if (minst==null){
            minst=new SharedPrefManager(context);
        }
        return minst;
    }



    public boolean userLogin(String id, String name, String phone){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ID,id);
        editor.putString(NAME,name);

        editor.putString(KEY_MOBILE,phone);



        editor.apply();
        return true;
    }

    public boolean Service_id(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_SID,id);



        editor.apply();
        return true;
    }


    public boolean tripid(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_TripID,id);



        editor.apply();
        return true;
    }



    public boolean Devicetoken(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Device,id);



        editor.apply();
        return true;
    }
    public boolean fatchdata( String mobile ){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(KEY_MOBILE,mobile);



        editor.apply();
        return true;
    }

    public static boolean getInstance(String setAddHome){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ADDHOME,setAddHome);


        return true;
    }




    public boolean FatchTrips(String booking_id, String user_id){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(KEY_BOOKING_ID,booking_id);
        editor.putString(KEY_USER_ID,user_id);



        editor.apply();
        return true;
    }





    public boolean isLogin(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }
    public boolean logout(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }




    public String getUserId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID,null);

    }

    public String getUserServiceId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SID,null);

    }

    public String getUserTripId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TripID,null);

    }



    public String getdeviceToken(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Device,null);

    }
    public String getUsername(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME,null);

    }
    public String getLastName(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_NAME,null);

    }


    public String getmobile(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE,null);

    }


    public String getLatitude() {
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LATITUDE,null);

    }
    public String getLongitude(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LONGITUDE,null);
    }



}
