package com.example.perfectshot;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.perfectshot.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    User user;
    SharedPreferences sharedPreferences;
    String prefKey = "Perfect Shot";

    Button btnLogin;
    Button logout;
    Button posts;

    boolean userLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        logout = findViewById(R.id.logout);
        posts = findViewById(R.id.posts);
        sharedPreferences = getSharedPreferences(prefKey, 0);

        getPermissions();

        Log.d("Main", "here");
    }

    public void startPost(View view){
        Intent i = new Intent(this, PostsActivity.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }

    public void startMap(View view){
        Intent i = new Intent(this, MapsActivity.class);
        if (user!=null)
        i.putExtra("User",user);
        startActivity(i);
    }

//    public void startCreate(View view){
//        Intent i = new Intent(this, CreatePostActivity.class);
//        if (user!=null)
//        i.putExtra("User",user);
//        startActivity(i);
//    }

    public void startLogin(View view){
        Intent i = new Intent(this, LoginActivity.class);
        if (user!=null)
        i.putExtra("User",user);
        startActivity(i);
    }

    private void getPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }else{
            Log.d("permissiontest","camera granted");
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }else{
            Log.d("permissiontest","location granted");
        }
    }



    /**
     * this method checks to see if there is a user logged in
     */
    public void checkLoginStatus(){
        String strUser = sharedPreferences.getString("User",null);
        User temp =  toObject(strUser);
        if (temp == null){
            userLoggedIn = false;
            Toast.makeText(this, "please log in or register", Toast.LENGTH_SHORT).show();
            logout.setEnabled(false);
            setButtons(false);
        }else{
            user = temp;
            Toast.makeText(this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();
            userLoggedIn = true;
            btnLogin.setEnabled(false);
            logout.setEnabled(true);
            setButtons(true);
        }
    }

    /**
     * this method will get called when a user clicks the logout button. it will just overwrite the user
     * field in shared preferences, and reset any button properties as needed. we could store all the
     * usernames that have logged out in a string set, so that we could suggest them when a user
     * logs in, but thats a V2 issue
     * @param v
     */
    public void logout(View v){
        if (user != null && user.getStatus()){
            user.setStatus(false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("User", null);
            editor.commit();
            Toast.makeText(this, "logged out "  +user.getUsername(), Toast.LENGTH_SHORT).show();
            btnLogin.setEnabled(true);
            logout.setEnabled(false);
            user = null;
            setButtons(false);
        }else{
            Toast.makeText(this, "logout failed!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setButtons(boolean boo){
        posts.setEnabled(boo);
    }

    /**
     * this method is used to take the stringified version of a user and create a new user object from
     * that string
     * @param strUser
     * @return
     */
    public User toObject(String strUser){
        if (strUser == null){
            User x = null;
            return x;
        }
        if (strUser.split(" ").length != 7){
            User w = null;
            return w;
        }
        User u = new User(strUser.split(" ")[3], strUser.split(" ")[4], strUser.split(" ")[0],strUser.split(" ")[1],strUser.split(" ")[2]);
        if (strUser.split(" ")[5].equals("true"))
            u.setStatus(true);
        else
            u.setStatus(false);
        try{
            u.id =Integer.parseInt(strUser.split(" ")[6]);
        }catch (Exception ex){

        }
        return u;
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkLoginStatus();
    }

}
