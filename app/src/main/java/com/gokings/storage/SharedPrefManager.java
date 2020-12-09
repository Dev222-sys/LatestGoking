package com.gokings.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.gokings.model.User_login;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private Context mCtx;
    private static  final String SHARED_PREF_NAME="my_shared_preff";


    public SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx)
    {
        if( mInstance==null)
        {
            mInstance=new SharedPrefManager(mCtx);

        }
        return  mInstance;
    }
    public  void saveuser(User_login user_login)
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("usertype",user_login.getUsertype());
        editor.putInt("status_code",user_login.getStatus_code());

        editor.apply();

    }
    public  boolean isLoggedin()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("status_code",-1 )!=-1;
    }
    public User_login getuser()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User_login user_login=new User_login(sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("usertype",null));

        return  user_login;
    }
    public   void clear()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
