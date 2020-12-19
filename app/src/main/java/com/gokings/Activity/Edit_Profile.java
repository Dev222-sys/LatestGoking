package com.gokings.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.ContactNumberActivity;
import com.gokings.OTPScreen;
import com.gokings.R;
import com.gokings.databasee.RetrofitClient;
import com.gokings.form;
import com.gokings.storage.SharedPrefManager;
import com.gokings.util;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;

public class Edit_Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String School, School_type;

    NeumorphButton button;
    EditText name;

    KProgressHUD pDialog;
    String id;
    Spinner primary1;
    Spinner coloredSpinner;
    NeumorphCardView cv_school_stage,cv_school_name,cv_spinner_stage,cv_spinner_name;
    TextView tv_school_name,tv_school_stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        util.blackiteamstatusbar(Edit_Profile.this, R.color.light_background);
        name = findViewById(R.id.name);
        cv_school_name=findViewById(R.id.cv_school_name);
        cv_school_stage=findViewById(R.id.cv_school_stage);
        tv_school_name=findViewById(R.id.tv_school_name);
        tv_school_stage=findViewById(R.id.tv_school_stage);

        cv_spinner_name=findViewById(R.id.cv_spinner_name);
        cv_spinner_stage=findViewById(R.id.cv_spinner_stage);
        cv_school_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv_school_name.setVisibility(View.GONE);
                cv_school_stage.setVisibility(View.GONE);
                cv_spinner_name.setVisibility(View.VISIBLE);
                cv_spinner_stage.setVisibility(View.VISIBLE);
            }
        });
        cv_school_stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv_school_name.setVisibility(View.GONE);
                cv_school_stage.setVisibility(View.GONE);
                cv_spinner_name.setVisibility(View.VISIBLE);
                cv_spinner_stage.setVisibility(View.VISIBLE);
            }
        });




        get_user_deatils();

        coloredSpinner = findViewById(R.id.school_name);
        // coloredSpinner.setPrompt("Select an item");
        primary1 = findViewById(R.id.Education_stage);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.maptype_array,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        coloredSpinner.setAdapter(adapter);
        coloredSpinner.setOnItemSelectedListener(Edit_Profile.this);


        ArrayAdapter primary2 = ArrayAdapter.createFromResource(
                this,
                R.array.primary,
                R.layout.color_spinner_layout
        );
        primary2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        primary1.setAdapter(primary2);
        primary1.setOnItemSelectedListener(Edit_Profile.this);


        //anyeca = findViewById(R.id.anyeca);

        button = (NeumorphButton) findViewById(R.id.Submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* ;*/


                // Toast.makeText(Edit_Profile.this, School+School_type+"", Toast.LENGTH_SHORT).show();


                update_profile();


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        name.clearFocus();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        // TODO Auto-generated method stub


        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.school_name) {
            School = parent.getItemAtPosition(position).toString();
            // Toast.makeText(this, School+"", Toast.LENGTH_SHORT).show();
        } else if (spinner.getId() == R.id.Education_stage) {
            School_type = parent.getItemAtPosition(position).toString();

            //Toast.makeText(this, School_type+"", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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

    public void get_user_deatils() {


        id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();
        loginByServer();
        showpDialog();


        //  Toast.makeText(context, Devicetoken+"", Toast.LENGTH_SHORT).show();


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().get_edit_profil(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                // Toast.makeText(Edit_Profile.this, response+"", Toast.LENGTH_SHORT).show();

                if (response.code() == 200) {

                    try {

                        s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                       // Toast.makeText(Edit_Profile.this, s + "", Toast.LENGTH_SHORT).show();
                        String username = jsonObject.getString("uname");
                        String school_name = jsonObject.getString("school_name");
                        String school_stage = jsonObject.getString("school_categories");
                        name.setText(username);
                        tv_school_name.setText(school_name);
                        tv_school_stage.setText(school_stage);


                        // Toast.makeText(Edit_Profile.this, school_name+school_stage+"", Toast.LENGTH_SHORT).show();


                        hidepDialog();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(Edit_Profile.this, "Please enter your details", Toast.LENGTH_SHORT).show();
                    hidepDialog();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                hidepDialog();

            }
        });

    }

    public void update_profile() {


        id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();
        String uname = name.getText().toString().trim();

        loginByServer();
        showpDialog();


        //  Toast.makeText(context, Devicetoken+"", Toast.LENGTH_SHORT).show();


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().update_profile(id, uname, School, School_type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                // Toast.makeText(Edit_Profile.this, response+"", Toast.LENGTH_SHORT).show();

                if (response.code() == 200) {

                    try {

                        s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Toast.makeText(Edit_Profile.this, "Details updated successfully   !!!!!", Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(Edit_Profile.this, Currnet_Location.class);
                        startActivity(in);

                        // Toast.makeText(Edit_Profile.this, school_name+school_stage+"", Toast.LENGTH_SHORT).show();


                        hidepDialog();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(Edit_Profile.this, "Server Problm", Toast.LENGTH_SHORT).show();
                    hidepDialog();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                hidepDialog();

            }
        });

    }


}

