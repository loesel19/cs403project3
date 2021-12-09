package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PostActivity extends AppCompatActivity {

    Button btnPostToMain;
    Button btnPostToSettings;

    Button btnMakePost;
    RecyclerView recPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnPostToMain = findViewById(R.id.btnPostToMain);
        btnPostToSettings = findViewById(R.id.btnPostToSettings);
        btnMakePost = findViewById(R.id.btnMakePost);
        recPosts = findViewById(R.id.recPosts);

        btnPostToMain.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnPostToSettings.setOnClickListener(v->{
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        });

    }
}