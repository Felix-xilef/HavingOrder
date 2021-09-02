package com.fatec.havingorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler hadler = new Handler();
        hadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToLogin();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Orders.class);
        startActivity(intent);
        finish();
    }
}