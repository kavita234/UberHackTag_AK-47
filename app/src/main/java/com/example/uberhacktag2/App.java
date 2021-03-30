package com.example.uberhacktag2;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;


public class App extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}