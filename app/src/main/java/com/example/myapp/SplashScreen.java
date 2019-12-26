package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {


    private long splashTime  = 3000L ;//3s
    private Handler myhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        myhandler = new Handler();
//        myhandler.postDelayed(new )
        startActivity(new Intent(this,LoginActivity.class));
    }
}
