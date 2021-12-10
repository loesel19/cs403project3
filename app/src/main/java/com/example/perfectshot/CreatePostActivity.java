package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CreatePostActivity extends AppCompatActivity {
    ImageView imgToPost;
    Button btnCamera, btnGallery, btnPost;
    private static final int CHOOSE_IMAGE = 100;
    private static final int CAPTURE_IMAGE = 101;
    Uri imageURI;
    Bitmap imageBitmap;
    boolean imageUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        imgToPost = findViewById(R.id.imgToPost);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnPost = findViewById(R.id.btnPost);

        imageUploaded = false;

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataValidate()){

                }
            }
        });

    }

    private boolean dataValidate(){
        boolean goodData = true;



        return goodData;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, CHOOSE_IMAGE);
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE){
            imageURI = data.getData();
            imgToPost.setImageURI(imageURI);
        }

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE){
            imageBitmap = (Bitmap)data.getExtras().get("data");
            imgToPost.setImageBitmap(imageBitmap);
        }
    }

}