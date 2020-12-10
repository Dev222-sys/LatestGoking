package com.gokings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gokings.Activity.MapsActivity;
import com.gokings.Activity.Showing_person_google;
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
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphTextView;

public class form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    NeumorphButton button;
    KProgressHUD pDialog;
    ImageView imageback;
    EditText radius;
    String School,School_type;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        util.blackiteamstatusbar(form.this,R.color.light_background);
      imageback=findViewById(R.id.imageback);
      radius=findViewById(R.id.radius);


      imageback.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(form.this,MapsActivity.class);
              startActivity(intent);
          }
      });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.maptype_array,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner coloredSpinner = findViewById(R.id.coloredSpinner);
        coloredSpinner.setAdapter(adapter);
        coloredSpinner.setOnItemSelectedListener(form.this);


        ArrayAdapter primary2 = ArrayAdapter.createFromResource(
                this,
                R.array.primary,
                R.layout.color_spinner_layout
        );
      primary2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner primary1= findViewById(R.id.primary);
      primary1.setAdapter( primary2);
      primary1.setOnItemSelectedListener(form.this);



        //anyeca = findViewById(R.id.anyeca);

        button = (NeumorphButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(form.this, School+School_type+"", Toast.LENGTH_SHORT).show();

               number_validation();

            }
        });


      /*  anyeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.setVisibility(View.VISIBLE);
                on.setVisibility(View.VISIBLE);
            }
        });*/



    }
    public void openActivity_create(){

        Intent intent = new Intent(this, create.class);
        startActivity(intent);
    }



    public void number_validation() {
        loginByServer();
        showpDialog();
        String id =SharedPrefManager.getInstans(getApplicationContext()).getUserId();

        final String radiuss= radius.getText().toString().trim();


        if (TextUtils.isEmpty(radiuss)) {
            radius.setError("Please Enter Your Searching Radius");
            radius.requestFocus();
            //util.showtoast(ContactNumberActivity.this,"Please enter Mobile Number");
            hidepDialog();
        } else {

            Call<ResponseBody> call= RetrofitClient
                    .getInstance()
                    .getApi().sendradius(id,radiuss,School,School_type);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String s=null;

                    if (response.code()==200) {

                        try {

                            s=response.body().string();

                           // Toast.makeText(form.this, s+"", Toast.LENGTH_SHORT).show();
                            Intent in=new Intent(form.this, Showing_person_google.class);
                            startActivity(in);

                            hidepDialog();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }

                    hidepDialog();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hidepDialog();
                    Toast.makeText(form.this, call.toString()+"", Toast.LENGTH_SHORT).show();
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

/*

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       // Toast.makeText(this,adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

        switch (adapterView.getId()) {
            case R.array.maptype_array:
                School = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(this, School+"", Toast.LENGTH_SHORT).show();
                break;
            case R.id.primary:
                School_type = adapterView.getItemAtPosition(i).toString();

//                Toast.makeText(this, School_type+"", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}*/
@Override
public void onItemSelected(AdapterView<?> parent, View v, int position,
                           long id){
    // TODO Auto-generated method stub


    Spinner spinner = (Spinner) parent;
    if(spinner.getId() ==R.id.coloredSpinner)
    {
        School = parent.getItemAtPosition(position).toString();
       // Toast.makeText(this, School+"", Toast.LENGTH_SHORT).show();
    }
    else if(spinner.getId() == R.id.primary)
    {
        School_type = parent.getItemAtPosition(position).toString();

        //Toast.makeText(this, School_type+"", Toast.LENGTH_SHORT).show();
    }

}

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



}