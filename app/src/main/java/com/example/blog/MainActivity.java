package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;

import Fragments.Sign_In_fragment;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    void init() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

              isFistTime();
            }
        }, 2000);
    }

    private void isFistTime() {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean("first_time", true);
        if (firstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first_time",false);
            editor.apply();
            startActivity(new Intent(getBaseContext(), OnBoardActivity.class));
            finish();
        } else {
            startActivity(new Intent(getBaseContext() , AuthActivity.class));
            finish();
        }
    }
}