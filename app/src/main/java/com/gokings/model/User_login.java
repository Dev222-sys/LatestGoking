package com.gokings.model;

public class User_login {
    private int status_code;
    private String usertype;

    public User_login(int status_code, String usertype) {
        this.status_code = status_code;
        this.usertype = usertype;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getUsertype() {
        return usertype;
    }
}
