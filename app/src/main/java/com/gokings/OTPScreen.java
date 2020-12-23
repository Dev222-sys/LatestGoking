package com.gokings;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gokings.Activity.Currnet_Location;
import com.gokings.Activity.School_Deatils;
import com.gokings.databasee.RetrofitClient;
import com.gokings.storage.SharedPrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPScreen extends AppCompatActivity {

    private EditText e1, e2, e3, e4;
    private ImageView image;
    private Button cont;
    KProgressHUD pDialog;
    String otp, mobile_no = null, name = null;
    TextView resend_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);
        util.blackiteamstatusbar(OTPScreen.this, R.color.light_background);

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);
        image = findViewById(R.id.image);
        cont = findViewById(R.id.cont);
        resend_otp=findViewById(R.id.resend_otp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            otp = bundle.getString("otp");
            mobile_no = bundle.getString("mobile_no");
            name = bundle.getString("name1");


            resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Resend_Otp();

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginByServer();
                showpDialog();
                otpvalidation();

                //startActivity(new Intent(OTPScreen.this, MapsActivity.class));
                // util.showtoast(OTPScreen.this,msg);
                hidepDialog();
            }
        });


        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e2.requestFocus();
                }
            }
        });
        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e3.requestFocus();
                }
            }
        });
        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e4.requestFocus();
                }
            }
        });
        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e4.requestFocus();
                }
            }
        });
    }





}
    private void otpvalidation() {

        String ee1, ee2, ee3, ee4;
        ee1 = e1.getText().toString();
        ee2 = e2.getText().toString();
        ee3 = e3.getText().toString();
        ee4 = e4.getText().toString();
        if (TextUtils.isEmpty(ee1)) {
            e1.setError("Please enter your otp");
            e1.requestFocus();
        } else if (TextUtils.isEmpty(ee2)) {
            e2.setError("Please enter your otp");
            e2.requestFocus();
        } else if (TextUtils.isEmpty(ee3)) {
            e3.setError("Please enter your otp");
            e3.requestFocus();
        } else if (TextUtils.isEmpty(ee4)) {
            e4.setError("Please enter your otp");
            e4.requestFocus();
        } else {


            String ed_otp = null;

            ed_otp = ee1.trim() + ee2.trim() + ee3.trim() + ee4.trim();

            if (ed_otp.equals(otp)) {
                //Toast.makeText(this, otp + name + mobile_no + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show();
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi().register(name, mobile_no);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        // Toast.makeText(OTPScreen.this, response.toString() + "", Toast.LENGTH_SHORT).show();
                        String s = null;

                        if (response.code() == 200) {
                            try {
                                s = response.body().string();
                                JSONObject jsonObject = new JSONObject(s);
                                String id = jsonObject.getString("id");
                                String phone=jsonObject.getString("phone");
                                String name=jsonObject.getString("name");
                                SharedPrefManager.getInstans(getApplicationContext()).userLogin(id,name,phone);
                                Intent intent = new Intent(OTPScreen.this, School_Deatils.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                hidepDialog();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Toast.makeText(OTPScreen.this, call.toString()+"", Toast.LENGTH_SHORT).show();
                        hidepDialog();
                    }
                });
            } else {
                Toast.makeText(this, "invalid  OTP", Toast.LENGTH_SHORT).show();
                hidepDialog();

            }
/*
            startActivity(new Intent(OTPScreen.this, MapsActivity.class));
*/


        }
    }

    private void loginByServer() {
        pDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void Resend_Otp()
             {

                 loginByServer();
                 showpDialog();
        Call<ResponseBody> call= RetrofitClient
                .getInstance()
                .getApi().send_otp(name,mobile_no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;

                if (response.code()==200) {

                    try {

                        s=response.body().string();
                        JSONObject jsonObject=new JSONObject(s);
                        String status_code=jsonObject.getString("status_code");

                        if (status_code.equals("0"))
                        {


                            hidepDialog();

                        }else {
                            Toast.makeText(OTPScreen.this, "OTP Sended", Toast.LENGTH_SHORT).show();

                             otp=jsonObject.getString("otp");


                            hidepDialog();

                        }
                        //    Toast.makeText(ContactNumberActivity.this, otp+"", Toast.LENGTH_SHORT).show();



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Toast.makeText(OTPScreen.this, "Please enter your details", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



            }
        });

    }


}
