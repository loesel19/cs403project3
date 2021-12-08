package com.example.perfectshot;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.example.perfectshot.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    User user;
    SharedPreferences sharedPreferences;
    String prefKey = "Perfect Shot";
    Button login;
    boolean userLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences(prefKey, 0);
        checkLoginStatus();

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


    /**
     * this method checks to see if there is a user logged in
     */
    public void checkLoginStatus(){
        String strUser = sharedPreferences.getString("User",null);
        User temp =  toObject(strUser);
        if (temp == null){
            userLoggedIn = false;
            Toast.makeText(this, "please log in or register", Toast.LENGTH_SHORT).show();
        }else{
            user = temp;
            Toast.makeText(this, "Welcome " + user.getFirst_name(), Toast.LENGTH_SHORT).show();
        }
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

}
