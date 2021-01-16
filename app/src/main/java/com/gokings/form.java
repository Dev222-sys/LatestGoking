package com.gokings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gokings.Activity.Currnet_Location;
import com.gokings.Activity.Edit_Profile;
import com.gokings.Activity.Showing_person_google;
import com.gokings.model.Online_person;
import com.gokings.storage.SharedPrefManager;
import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import soup.neumorphism.NeumorphButton;

public class
form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    NeumorphButton button;
    KProgressHUD pDialog;
    ImageView imageback;
    EditText radius;
    String School,School_type,locationn;

    LatLng latLng;
    String name,phone,lat,longt;
    ArrayList namelist = new ArrayList();
    ArrayList<Online_person> online_person = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        util.blackiteamstatusbar(form.this,R.color.light_background);

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //setting the title
      toolbar.setTitle(" ");
      //placing toolbar in place of actionbar
      setSupportActionBar(toolbar);

      imageback=findViewById(R.id.imageback);
      radius=findViewById(R.id.radius);


      imageback.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(form.this, Currnet_Location.class);
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


        ArrayAdapter location = ArrayAdapter.createFromResource(
                this,
                R.array.location,
                R.layout.color_spinner_layout
        );
        location.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner location1= findViewById(R.id.location);
        location1.setAdapter( location);
        location1.setOnItemSelectedListener(form.this);





        //anyeca = findViewById(R.id.anyeca);

        button = (NeumorphButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              number_validation();
              //  Toast.makeText(form.this, locationn+"", Toast.LENGTH_SHORT).show();


            }
        });





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

            Bundle basket = new Bundle();
            basket.putString("radiuss", radiuss);
            basket.putString("School", School);
            basket.putString("School_type", School_type);
            basket.putString("location", locationn);
            Intent intent = new Intent(form.this, Showing_person_google.class);
            intent.putExtras(basket);
            startActivity(intent);


            //Toast.makeText(form.this, radiuss+School+School_type+"", Toast.LENGTH_SHORT).show();
            hidepDialog();

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

    else if(spinner.getId() == R.id.location)
    {
        String location = parent.getItemAtPosition(position).toString();
        if (location.equals("Current Location"))
        {
            locationn="clocation";

        }else {
            locationn="hlocation";

        }


    }

}

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.edit_profile) {

            Intent intent = new Intent(form.this, Edit_Profile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}