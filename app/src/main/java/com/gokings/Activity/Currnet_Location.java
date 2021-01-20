package com.gokings.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.R;
import com.gokings.databasee.RetrofitClient;
import com.gokings.form;
import com.gokings.storage.SharedPrefManager;
import com.gokings.util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Currnet_Location extends AppCompatActivity {
    SupportMapFragment srf;
    double lat;
    double longt;

    FusedLocationProviderClient client;
    Button Searching,edit_profile;
    CardView maps;
    KProgressHUD pDialog;
    RadioButton cLocation,hLocation;
    String address_type ="null";
    RadioGroup radiogroup;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currnet__location);
        Searching=findViewById(R.id.Searching);
        maps=findViewById(R.id.maps);
        edit_profile=findViewById(R.id.edit_profile);

        cLocation=findViewById(R.id.cLocation);
        hLocation=findViewById(R.id.hLocation);
        radiogroup=findViewById(R.id.radiogroup);
        location=findViewById(R.id.location);
        util.blackiteamstatusbar(Currnet_Location.this, R.color.light_background);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.Flag_F);
        srf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        Searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();


            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Currnet_Location.this, Edit_Profile.class);
                startActivity(intent);
            }
        });

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getmylocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();


                    }
                }).check();




    }

    public void getmylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                srf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        //Toast.makeText(Currnet_Location.this, "yes", Toast.LENGTH_SHORT).show();
                        if (location!=null)
                        {

                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            lat=location.getLatitude();
                            longt=location.getLongitude();
                            MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("You are here.....!!");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                            maps.setVisibility(View.VISIBLE);
                        }else
                        {
                            showAlert();






                        }


                    }
                });

            }
        });



    }

    public  void location()
    {


        if (hLocation.isChecked()) {

            address_type = "hlocation";

            sendlatlong();

        }else if (cLocation.isChecked()) {

            address_type = "clocation";
            sendlatlong();

        }
        else {

            location.setError("Please Select");
            location.requestFocus();
            Toast.makeText(this, "Please Select Location Type...!!", Toast.LENGTH_SHORT).show();

        }

    }



    public  void sendlatlong()
    {
       loginByServer();
        showpDialog();

    //  Toast.makeText(this, address_type+ "", Toast.LENGTH_SHORT).show();

        String id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();
        String latt=String.valueOf(lat);
        String longtt=String.valueOf(longt);


        //Toast.makeText(Currnet_Location.this, latt+longtt+"", Toast.LENGTH_SHORT).show();

        Call<ResponseBody> call= RetrofitClient
                .getInstance()
                .getApi().SendLatlong(id,address_type,latt,longtt);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;

                if (response.code()==200) {
                    try {
                        s=response.body().string();
                        // Toast.makeText(MapsActivity.this, s+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Currnet_Location.this, form.class);
                        startActivity(intent);
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

    private void showAlert(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        dialog.setTitle("Enable Location")
                .setMessage("Your Location Setting is Set to 'off'.\nPlease Enable Location to" + "Use this App")
                .setPositiveButton("Location Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();

/*
        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();*/
    }
}