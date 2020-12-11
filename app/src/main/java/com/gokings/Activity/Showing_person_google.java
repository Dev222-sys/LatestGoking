package com.gokings.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gokings.MainActivity;
import com.gokings.R;
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

import java.util.ArrayList;

public class Showing_person_google extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> arrayList = new ArrayList<>();

    LatLng l1 = new LatLng(28.54949553440099, 77.20359201437427);
    LatLng l2 = new LatLng(28.5497706125795, 77.20353011890617);
    LatLng l3 = new LatLng(28.552288405715974, 77.20270345431017);
    LatLng l4 = new LatLng(28.55988210142341, 77.20577847637541);
    ArrayList<String> title;
    ArrayList<String> Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_person_google);
        util.blackiteamstatusbar(Showing_person_google.this, R.color.light_background);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        title = new ArrayList<>();
        Name = new ArrayList<>();


        arrayList.add(l1);
        arrayList.add(l2);
        arrayList.add(l3);
        arrayList.add(l4);

        title.add("8287018255");
        title.add("9710000528");
        title.add("9098765432");
        title.add("9087643222");

        Name.add("dev");
        Name.add("Mirran");
        Name.add("Rahul");
        Name.add("Rohit");


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < title.size(); j++) {
                for (int k = 0; k < Name.size(); k++) {


                    //drawMarkerWithCircle(arrayList.get(i));
                    mMap.addMarker(new MarkerOptions()
                                    .position(arrayList.get(i))
                                    .title(String.valueOf(Name.get(i)))
                                    .snippet(title.get(i))
                                    .anchor(0.5f, 0.5f)


                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo2)));


                    );

                }

            }
            float zoomLevel = 17.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(i), zoomLevel));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
        }
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

}