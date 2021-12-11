package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostsActivity extends AppCompatActivity {

    Button btnMakePost;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        btnMakePost = findViewById(R.id.btnMakePost);

        btnMakePost.setOnClickListener(view -> startCreate(view));


        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");
    }


    public void startCreate(View view){
        Intent i = new Intent(this, CreatePostActivity.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }


    public void startSettings(View view){
        Intent i = new Intent(this, Settings.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }

    public void backBtn(View v){
        super.finish();
    }
}