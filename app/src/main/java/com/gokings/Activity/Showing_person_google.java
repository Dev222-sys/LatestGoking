package com.gokings.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.MainActivity;
import com.gokings.R;
import com.gokings.databasee.RetrofitClient;
import com.gokings.form;
import com.gokings.model.Online_person;
import com.gokings.storage.SharedPrefManager;
import com.gokings.util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Showing_person_google extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> arrayList = new ArrayList<>();

    ArrayList namelist = new ArrayList();
    ArrayList<Online_person> online_person = new ArrayList();

    Online_person online_persons;
    KProgressHUD pDialog;
    LatLng latLng;
    String name, phone, lat, longt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_person_google);
        util.blackiteamstatusbar(Showing_person_google.this, R.color.light_background);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
      //  number_validation();


        mMap = googleMap;

        loginByServer();
        showpDialog();
        String id = SharedPrefManager.getInstans(getApplicationContext()).getUserId();

        String radiuss = null, School = null, School_type = null;
        String location = null;


        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

            //ObtainBundleData in the object
            radiuss = bundle.getString("radiuss");

            School = bundle.getString("School");
            School_type = bundle.getString("School_type");

            location = bundle.getString("location");

          Toast.makeText(this, location + "", Toast.LENGTH_SHORT).show();
            //Do something here if data  received
        } else {
            //Do something here if data not received
        }


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
    .getApi().sendradius(id, radiuss, School, School_type,location);
//        .getApi().sendradius(id, radiuss, School, School_type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                //              Toast.makeText(Showing_person_google.this, response.code() + "", Toast.LENGTH_SHORT).show();

                if (response.code() == 200) {

//                    Toast.makeText(Showing_person_google.this, response.body().toString() + "", Toast.LENGTH_SHORT).show();
                    try {

                        s = response.body().string();

                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            online_persons=new Online_person();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            name = jsonObject1.getString("uname");
                            phone = jsonObject1.getString("phone");
                            lat = jsonObject1.getString("latitude");
                            longt = jsonObject1.getString("longitude");

                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(longt));

                            mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(name)
                                            .snippet(phone)
                                            .anchor(0.5f, 0.5f)
                            );


                            namelist.add(name);
                           // Toast.makeText(Showing_person_google.this, phone + name + lat + "   " + longt + "", Toast.LENGTH_SHORT).show();
                            float zoomLevel = 11.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        }




                        // Toast.makeText(Showing_person_google.this, s+"", Toast.LENGTH_SHORT).show();
                        hidepDialog();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 404) {


                        Toast.makeText(Showing_person_google.this, "User not found. Maybe try increasing your radius !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Showing_person_google.this, form.class);
                        startActivity(intent);


                }
                hidepDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Toast.makeText(Showing_person_google.this,  "Server Error", Toast.LENGTH_SHORT).show();
            }
        });








        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                final String PhoneNum = marker.getTitle();
                View view1 = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
                TextView name = view1.findViewById(R.id.name);
                TextView number = view1.findViewById(R.id.number);
                RelativeLayout call = view1.findViewById(R.id.call);
                number.setText(marker.getSnippet());
                name.setText(PhoneNum);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Showing_person_google.this, "call", Toast.LENGTH_SHORT).show();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + Uri.encode(marker.getSnippet().trim())));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                });


                BottomSheetDialog dialog = new BottomSheetDialog(Showing_person_google.this);
                dialog.setContentView(view1);
                dialog.show();
                return false;
            }
        });
    }

    private void drawMarkerWithCircle(LatLng position) {
        Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(position.latitude, position.longitude))
                        .radius(50)
                        .strokeColor(Color.RED)
                /*.fillColor(Color.BLUE)*/);



      /*  double radiusInMeters = 100.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mMap.addMarker(markerOptions);*/
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


