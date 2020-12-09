package com.gokings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.Activity.MapsActivity;
import com.gokings.Activity.Terms_Service;
import com.gokings.databasee.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphButton;


public class ContactNumberActivity extends AppCompatActivity {
    private  NeumorphButton button;
    private EditText number;
    private EditText name;
    private CheckBox  checkbox;
    KProgressHUD pDialog;
TextView terms;

    protected SwipeRefreshLayout swipeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);
        util.blackiteamstatusbar(ContactNumberActivity.this,R.color.light_background);

        initview();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openActivity_o_t_p_screen();

            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByServer();
                showpDialog();

                startActivity(new Intent(ContactNumberActivity.this, Terms_Service.class));
             //   hidepDialog();

            }
        });
        
    }
    public   void initview()
    {
        button = (NeumorphButton) findViewById(R.id.button);
        number=findViewById(R.id.number);
        checkbox=findViewById(R.id.checkbox);
        terms=findViewById(R.id.terms);
        name=findViewById(R.id.name);



    }
    public void openActivity_o_t_p_screen() {
       // Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
       // util.showtoast(ContactNumberActivity.this,"please check Number");
        number_validation();


    }

    public void privacyPolicyClick(View view) {
        loginByServer();
        showpDialog();

        startActivity(new Intent(ContactNumberActivity.this, PrivacyPolicyActivity.class));
       // hidepDialog();

    }

    public void number_validation() {
        loginByServer();
        showpDialog();


        //  Toast.makeText(context, Devicetoken+"", Toast.LENGTH_SHORT).show();
      //  swipeView.setRefreshing(true);
        final String mobile= number.getText().toString().trim();

        final String name1=name.getText().toString().trim();



        if (TextUtils.isEmpty(name1)) {
            name.setError("Please enter your full name");
            name.requestFocus();
            //util.showtoast(ContactNumberActivity.this,"Please enter Mobile Number");
            hidepDialog();
        }

        else  if (mobile.length()!=10)
        {
            number.setError("Please enter a valid mobile ");
            number.requestFocus();
            util.showtoast(ContactNumberActivity.this,"Please enter a valid mobile Number");

            hidepDialog();
        }
        else if(!checkbox.isChecked()){
            checkbox.setError("Please checked it");
            util.showtoast(ContactNumberActivity.this,"Please checked it");

            checkbox.requestFocus();
            hidepDialog();
        }

        else {
/*
            Bundle bundle = new Bundle();
            bundle.putString("otp","1234");
            bundle.putString("mobile_no",mobile);
            bundle.putString("name1",name1);


            Intent intent = new Intent(ContactNumberActivity.this,OTPScreen.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            intent.putExtras(bundle);
            startActivity(intent);
            hidepDialog();

*/

           /* Intent intent = new Intent(ContactNumberActivity.this, OTPScreen.class);
            startActivity(intent);
            hidepDialog();*/


            Call<ResponseBody> call= RetrofitClient
                    .getInstance()
                    .getApi().send_otp(name1,mobile);
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

                                    String status=jsonObject.getString("usertype");
                                    String name=jsonObject.getString("user_name");


                                    Toast.makeText(ContactNumberActivity.this, status+name+"", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ContactNumberActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                    hidepDialog();

                                }else {

                                String otp=jsonObject.getString("otp");
                                String status=jsonObject.getString("status");
                                Bundle bundle = new Bundle();
                                bundle.putString("otp",otp);
                                bundle.putString("mobile_no",mobile);
                                bundle.putString("name1",name1);

                                Intent intent = new Intent(ContactNumberActivity.this,OTPScreen.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                Toast.makeText(ContactNumberActivity.this, status+"", Toast.LENGTH_SHORT).show();

                                hidepDialog();

                                }
                            //    Toast.makeText(ContactNumberActivity.this, otp+"", Toast.LENGTH_SHORT).show();



                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        else
                        {
                            Toast.makeText(ContactNumberActivity.this, "Please enter your details", Toast.LENGTH_SHORT).show();

                        }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {



                }
            });

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


    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}