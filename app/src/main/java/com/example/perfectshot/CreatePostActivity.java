package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {
    ImageView imgToPost;
    Button btnCamera, btnGallery, btnPost, btnLocation;
    private static final int CHOOSE_IMAGE = 100;
    private static final int CAPTURE_IMAGE = 101;
    private static final int LOCATION = 102;
    String currentPhotoPath, description;
    int initialRating;
    Uri imageURI;
    Bitmap imageBitmap;
    boolean imageUploaded;
    EditText tvDesc;
    TextView txtRatingValue;
    User user;
    Double lat;
    Double lon;


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

        //initialize these variables
        imageUploaded = false;
        lat = 0.0;
        lon = 0.0;

        queue =  Volley.newRequestQueue(getApplicationContext());

        btnGallery.setOnClickListener(view -> openGallery());

        btnCamera.setOnClickListener(view -> openCamera());

        btnPost.setOnClickListener(view -> {
            description = tvDesc.getText() + "";
            if(dataValidate()){
                send_data();
            }
        });
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");

    }

    //Evan's magic
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
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                params.put("image", new DataPart(imagename + ".png", byteArrayOutputStream.toByteArray()));
                return params;
            }
        };
        queue.add(volleyMultipartRequest);
    }

    //Evan's magic
    public void send_post(int imageID){
        Post post  = new Post(user.id, imageID, tvDesc.getText()+"", lat, lon);
        Log.d("MEMEME", post.toJson().toString());


        JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_post", post.toJson(), response ->{
            Intent i = new Intent(this, PostsActivity.class);
            startActivity(i);
            //finish();
        }, error ->{
            Toast.makeText(this,"error posting: " + error.toString(), Toast.LENGTH_LONG).show();
            finish();
        });

        this.queue.add(r);
    }

    private boolean dataValidate(){
        if(!imageUploaded){
            Toast.makeText(this,"Please upload a photo",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(description.equals("")){
            Toast.makeText(this,"Please add a description",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lat == 0.0 && lon == 0.0){
            Toast.makeText(this,"Please choose a location",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, CHOOSE_IMAGE);
    }

    //Wow, this is so much more complicated than the gallery.
    //And it isnt even that many lines of code
    //But dear lord was this painful to write
    //Anyway, we pass a file location/URI to the intent-
    //This lets the intent SAVE the photo to a file we can pull later
    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("fileerror","the file didnt work");
        }
        //Hypothetically it shant be null...
        if(photoFile != null){
            Uri photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
            camera.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(camera,CAPTURE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //If they chose a image from their gallery we simply HAVE the URI
        if(resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE){
            imageURI = data.getData();
            imgToPost.setImageURI(imageURI);
            imageUploaded = true;
        }
        //If they take a pic themself, we need to grab the URI from the file system
        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE){
            File file = new File(currentPhotoPath);
            imageURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", file);
            imgToPost.setImageURI(imageURI);
            imageUploaded = true;
        }

        if(resultCode == RESULT_OK && requestCode == LOCATION) {
            LatLng latLng = (LatLng) data.getParcelableExtra("location");
            lat =  latLng.latitude;
            lon =  latLng.longitude;
        }
    }

    public void pickLocation(View v){
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("create",true);
        startActivityForResult(i,LOCATION);
    }

    //Helper class that deals with grabbing a unique file name based on the current time and date
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}