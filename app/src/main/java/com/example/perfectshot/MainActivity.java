package com.example.perfectshot;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.perfectshot.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    User user;
    SharedPreferences sharedPreferences;
    String prefKey = "Perfect Shot";

    Button btnLogin;
    Button logout;
    Button btnMap;
    Button posts;
    Button createPost;
    Button settings;

    boolean userLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        logout = findViewById(R.id.logout);
        btnMap = findViewById(R.id.btnMap);
        posts = findViewById(R.id.posts);
        createPost = findViewById(R.id.createPost);
        settings = findViewById(R.id.settings);
        sharedPreferences = getSharedPreferences(prefKey, 0);
        Log.d("Main", "here");
    }

    public void startPost(View view){
        Intent i = new Intent(this, PostsActivity.class);
        i.putExtra("User",user.toString());
        startActivity(i);
    }

    public void startMap(View view){
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("User",user.toString());
        startActivity(i);
    }

    public void startCreate(View view){
        Intent i = new Intent(this, CreatePostActivity.class);
        i.putExtra("User",user.toString());
        startActivity(i);
    }

    public void startLogin(View view){
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("User",user.toString());
        startActivity(i);
    }

    public void startSettings(View view){
        Intent i = new Intent(this, Settings.class);
        i.putExtra("User",user.toString());
        startActivity(i);
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
        btnMap.setEnabled(boo);
        posts.setEnabled(boo);
        createPost.setEnabled(boo);
        settings.setEnabled(boo);
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
        if (strUser.split(" ").length != 6){
            User w = null;
            return w;
        }
        User u = new User(strUser.split(" ")[3], strUser.split(" ")[4], strUser.split(" ")[0],strUser.split(" ")[1],strUser.split(" ")[2]);
        if (strUser.split(" ")[5].equals("true"))
            u.setStatus(true);
        else
            u.setStatus(false);
        return u;
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkLoginStatus();
    }

}
