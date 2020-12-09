package com.gokings.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gokings.ContactNumberActivity;
import com.gokings.OTPScreen;
import com.gokings.R;
import com.gokings.databasee.RetrofitClient;
import com.gokings.form;
import com.gokings.util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    // private Spinner spinner;

    private GoogleApiClient mgoogleApiClient;
    private Location mlocation;
    private LocationManager mLocationManager;
    private LocationRequest mlocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2000;
    private long FASTER_INTERVAL = 5000;

    private LocationManager locationManager;
    private LatLng latLng;
    private boolean ispermission;

    NeumorphButton search;
    String msg;
    String lat,lon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        util.blackiteamstatusbar(this, R.color.light_background);
        search = findViewById(R.id.Searching);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(MapsActivity.this, msg + "", Toast.LENGTH_SHORT).show();
               /* Toast.makeText(MapsActivity.this, lat +lon+ "", Toast.LENGTH_SHORT).show();*/
                sendlatlong();
                Intent intent = new Intent(MapsActivity.this, form.class);
                startActivity(intent);

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (requestSinglePermission()) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //spinner = findViewById(R.id.spinner);
           /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;

                        case 1:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                            break;
                        case 2:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 3:
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;

                        case 4:
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        default:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
*/

        }


        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation();

    }

    private boolean checkLocation() {
        if (!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location").
                setMessage("your location Settings is Set to 'off'.\n Please Enable Location to" + "use this App")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
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

    }

    private boolean isLocationEnabled() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ispermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        if (requestSinglePermission()) {
                            ispermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
        return ispermission;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();

        mlocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
        if (mlocation == null) {
            startLocationUpdates();

        } else {
            // Toast.makeText(this, "Location Not Detected", Toast.LENGTH_SHORT).show();
        }

    }

    private void startLocationUpdates() {
        mlocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTER_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, mlocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        msg = "update Location" +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg + "", Toast.LENGTH_SHORT).show();

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat=String.valueOf(location.getLatitude());
        lon=String.valueOf(location.getLongitude());




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mgoogleApiClient != null) {
            mgoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this)
                .setTitle("Alert")
                .setCancelable(false)
                .setMessage("Are your sure want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(MapsActivity.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();


        /*Intent intent = new Intent(getApplicationContext(), ContactNumberActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/
    }

    public  void sendlatlong()
    {
        Toast.makeText(MapsActivity.this, lat +lon+ "", Toast.LENGTH_SHORT).show();

        Call<ResponseBody> call= RetrofitClient
                .getInstance()
                .getApi().SendLatlong(lat,lon);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;

                if (response.code()==200) {



                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



            }
        });

    }

}