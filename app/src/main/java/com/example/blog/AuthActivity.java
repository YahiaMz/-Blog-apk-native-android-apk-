package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import Fragments.Sign_In_fragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(com.example.blog.R.id.frameLayout , new Sign_In_fragment()).commit();

    }
}