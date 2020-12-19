package com.gokings.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class School_Deatils extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    String School,School_type;
    NeumorphButton  Submit;

    KProgressHUD pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__deatils);
        Submit=findViewById(R.id.Submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* String id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();
                Toast.makeText(School_Deatils.this, id+"", Toast.LENGTH_SHORT).show();*/
                send_school_deatils();


            }
        });
        util.blackiteamstatusbar(School_Deatils.this,R.color.light_background);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.maptype_array,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner coloredSpinner = findViewById(R.id.school_name);
        coloredSpinner.setAdapter(adapter);
        coloredSpinner.setOnItemSelectedListener(School_Deatils.this);


        ArrayAdapter primary2 = ArrayAdapter.createFromResource(
                this,
                R.array.primary,
                R.layout.color_spinner_layout
        );
        primary2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner primary1= findViewById(R.id.Education_stage);
        primary1.setAdapter( primary2);
        primary1.setOnItemSelectedListener(School_Deatils.this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id){
        // TODO Auto-generated method stub


        Spinner spinner = (Spinner) parent;
        if(spinner.getId() ==R.id.school_name)
        {
            School = parent.getItemAtPosition(position).toString();

            // Toast.makeText(this, School+"", Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId() == R.id.Education_stage)
        {
            School_type = parent.getItemAtPosition(position).toString();
            //Toast.makeText(this, School_type+"", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void send_school_deatils() {
        loginByServer();
        showpDialog();
        String id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();




            Call<ResponseBody> call= RetrofitClient
                    .getInstance()
                    .getApi().send_school_deatils(id,School,School_type);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String s=null;

                    if (response.code()==200) {

                        try {

                            s=response.body().string();
                            JSONObject jsonObject=new JSONObject(s);
                            String msg=jsonObject.getString("status");

                                Toast.makeText(School_Deatils.this, msg+"", Toast.LENGTH_SHORT).show();
                            Intent in=new Intent(School_Deatils.this, Currnet_Location.class);
                            startActivity(in);

                            hidepDialog();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    hidepDialog();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hidepDialog();
                    Toast.makeText(School_Deatils.this, call.toString()+"", Toast.LENGTH_SHORT).show();
                }
            });






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

}