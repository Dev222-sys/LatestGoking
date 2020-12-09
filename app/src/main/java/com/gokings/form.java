package com.gokings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphTextView;

public class form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    NeumorphButton button;
    KProgressHUD pDialog;
    ImageView imageback;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        util.blackiteamstatusbar(form.this,R.color.light_background);
      imageback=findViewById(R.id.imageback);
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
                loginByServer();
                showpDialog();

              Intent in=new Intent(form.this, Showing_person_google.class);
                startActivity(in);
            hidepDialog();

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this,adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}