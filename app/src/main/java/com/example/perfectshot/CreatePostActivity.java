package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {
    ImageView imgToPost;
    Button btnCamera, btnGallery, btnPost;
    private static final int CHOOSE_IMAGE = 100;
    private static final int CAPTURE_IMAGE = 101;
    Uri imageURI;
    Bitmap imageBitmap;
    boolean imageUploaded;
    EditText tvDesc;
    SeekBar skbRating;
    TextView txtRatingValue;
    User user;


    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        imgToPost = findViewById(R.id.imgToPost);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnPost = findViewById(R.id.btnPost);
        tvDesc = findViewById(R.id.txtDescription);
        txtRatingValue = findViewById(R.id.txtRatingValue);



        imageUploaded = false;

        queue =  Volley.newRequestQueue(getApplicationContext());

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
                    send_data();
                }
            }
        });
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");

    }

    public void send_data(){
        VolleyMultiPart volleyMultipartRequest = new VolleyMultiPart(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/image_upload",
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        send_post(obj.getInt("imageid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("GotError",""+error.getMessage())) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmap = null;
                if (imageURI != null){
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    bitmap = imageBitmap;
                }

                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                params.put("image", new DataPart(imagename + ".png", byteArrayOutputStream.toByteArray()));
                return params;
            }
        };
        queue.add(volleyMultipartRequest);
    }

    public void send_post(int imageID){
        //Todo: author ID is hardcoded!
        Post post  = new Post(user.id, imageID, tvDesc.getText()+"", 0, 0);
        Log.d("MEMEME", post.toJson().toString());


        JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_post", post.toJson(), response ->{
            finish();
        }, error ->{
            Toast.makeText(this,"error posting: " + error.toString(), Toast.LENGTH_LONG).show();
            finish();
        });

        this.queue.add(r);
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