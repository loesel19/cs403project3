package com.example.perfectshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfectshot.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPost(View view){
        Intent i = new Intent(this, PostActivity.class);
        startActivity(i);
    }

    public void startMap(View view){
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void startLogin(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void startSettings(View view){
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }
}
