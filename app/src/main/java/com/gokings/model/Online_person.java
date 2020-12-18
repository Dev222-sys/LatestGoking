package com.gokings.model;

import com.google.android.gms.maps.model.LatLng;

public class Online_person {
    private String name,mobile,lat,longt;
    private LatLng latLng;

    public Online_person(String name, String mobile, String lat, String longt,LatLng latLng) {
        this.name = name;
        this.latLng=latLng;
        this.mobile = mobile;
        this.lat = lat;
        this.longt = longt;
    }

    public Online_person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }
}
