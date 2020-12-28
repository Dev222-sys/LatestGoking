package com.gokings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.Activity.Currnet_Location;
import com.gokings.Activity.Terms_Service;
import com.gokings.databasee.RetrofitClient;
import com.gokings.storage.SharedPrefManager;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphButton;


public class ContactNumberActivity extends AppCompatActivity {
    private NeumorphButton button;
    private EditText number;
    private EditText name;
    private CheckBox checkbox;
    KProgressHUD pDialog;
    TextView terms;

    protected SwipeRefreshLayout swipeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);
        util.blackiteamstatusbar(ContactNumberActivity.this, R.color.light_background);

        initview();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_validation();

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

    public void initview() {
        button = (NeumorphButton) findViewById(R.id.button);
        number = findViewById(R.id.number);
        checkbox = findViewById(R.id.checkbox);
        terms = findViewById(R.id.terms);
        name = findViewById(R.id.name);


    }


    public void privacyPolicyClick(View view) {
        loginByServer();
        showpDialog();

        startActivity(new Intent(ContactNumberActivity.this, PrivacyPolicyActivity.class));


    }

    public void number_validation() {
        loginByServer();
        showpDialog();


        final String mobile = number.getText().toString().trim();


        //Toast.makeText(ContactNumberActivity.this, codee+"", Toast.LENGTH_SHORT).show();


        final String name1 = name.getText().toString().trim();


        if (TextUtils.isEmpty(name1)) {
            name.setError("Please enter your full name");
            name.requestFocus();
            //util.showtoast(ContactNumberActivity.this,"Please enter Mobile Number");
            hidepDialog();
        } else if (mobile.length() != 9) {
            number.setError("Please enter a valid mobile Number Without Country Code !!! ");
            number.requestFocus();
            // util.showtoast(ContactNumberActivity.this,"Please enter a valid mobile Number");

            hidepDialog();
        } else if (!checkbox.isChecked()) {
            checkbox.setError("Please checked it");
            //  util.showtoast(ContactNumberActivity.this,"Please checked it");

            checkbox.requestFocus();
            hidepDialog();
        } else {

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi().send_otp(name1, mobile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String s = null;

                    if (response.code() == 200) {

                        try {

                            s = response.body().string();
                            JSONObject jsonObject = new JSONObject(s);
                            String status_code = jsonObject.getString("status_code");

                            if (status_code.equals("0")) {

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("user_name");
                                String phone = jsonObject.getString("phone");

                                SharedPrefManager.getInstans(getApplicationContext()).userLogin(id, name, phone);


                                //Toast.makeText(ContactNumberActivity.this, id+name+"", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ContactNumberActivity.this, Currnet_Location.class);
                                startActivity(intent);
                                hidepDialog();

                            } else {

                                String otp = jsonObject.getString("otp");
                                String status = jsonObject.getString("status");
                                Bundle bundle = new Bundle();
                                bundle.putString("otp", otp);
                                bundle.putString("mobile_no", mobile);
                                bundle.putString("name1", name1);

                                Intent intent = new Intent(ContactNumberActivity.this, OTPScreen.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                Toast.makeText(ContactNumberActivity.this, status + "", Toast.LENGTH_SHORT).show();

                                hidepDialog();

                            }
                            //    Toast.makeText(ContactNumberActivity.this, otp+"", Toast.LENGTH_SHORT).show();


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
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