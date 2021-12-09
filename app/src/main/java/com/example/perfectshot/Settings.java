package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Button btnSaveSettings;
    Button btnCancelSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnCancelSettings = findViewById(R.id.btnCancelSettings);

        btnSaveSettings.setOnClickListener(v->{
            //Todo: Saving settings to systemprefs once we have them
            Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
            backToMain();
        });

        btnCancelSettings.setOnClickListener(v->{
            Toast.makeText(this, "Changes discarded...", Toast.LENGTH_SHORT).show();
            backToMain();
        });
    }

    void backToMain(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}