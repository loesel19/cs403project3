package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.perfectshot.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    //views
    EditText username;
    EditText password;
    EditText name;
    EditText email;

    //user objects
    User user;
    UserDAO dao;

    String msg;

    RequestQueue queue;
    String postURL;
    String requestURL;
    JSONObject postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //assign views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        queue = Volley.newRequestQueue(this);
        postURL= "https://frozen-reaches-15850.herokuapp.com/new_user";

    }

    /**
     * this method will check to see if the input fields contain valid inputs, and if they do
     * it tries to add the new user to the Database on the server side.
     * if a positive response a message will be displayed and the user will be taken to the
     * login activity, if it is a negative response/ volley error a message will be displayed
     * and the username and email fields will be reset
     * @param v
     */
    public void register(View v) throws InterruptedException, ExecutionException, AuthFailureError {
        if (validateInputs()){
//
            postData = new JSONObject();
            try {
                postData.put("first_name", user.getFirst_name());
                postData.put("last_name", user.getLast_name());
                postData.put("username", user.getUsername());
                postData.put("password", user.getPassword());
                postData.put("email",user.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postURL, postData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("User", "add user: " + response.toString());
                    Toast.makeText(getApplicationContext(), "Registration complete", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("User", "add user/e: " + error.toString());
                    Toast.makeText(getApplicationContext(), "try new username/ email", Toast.LENGTH_LONG).show();
                    username.setText("");
                    email.setText("");
                }
            });
            queue.add(jsonObjectRequest);

            }
        }


    /**
     * this method will call validation methods for all the inputs and then returns true if inputs are valid
     * and creates a new user object to be added into the database
     * @return
     */
    public boolean validateInputs(){
        //strings to validate
        String sName = name.getText().toString();
        String sEmail = email.getText().toString();
        String sUsername = username.getText().toString();
        String sPassword = password.getText().toString();

        if (!((validateName(sName)) && validateEmail(sEmail) && validatePassword(sPassword) && validateUsername(sUsername))) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            user = new User(sUsername, sPassword, sName.split(" ")[0], sName.split(" ")[1], sEmail, -1);
            Log.d("User", "new user object created/ inputs were valid");
            return true;
        }


    }

    /**
     * this method will take in the name the user entered and check to see if it is valid i.e. only
     * contains letters
     * @param sname
     * @return
     */
    public boolean validateName(String sname){
        if (sname.split(" ").length != 2){
            Log.d("User", "not first last");
            msg = "please enter first and last name";
            this.name.setText("");
            return false;
        }
        String first = sname.split(" ")[0];
        String last = sname.split(" ")[1];
        if (first.length() == 0){
            msg = "first name cannot be blank";
            Log.d("User", "first name blank");
            return false;
        }else if (last.length() == 0){
            msg = "last name cannot be blank";
            Log.d("User", "last name blank");
            return false;
        }
        else if (!first.matches("[A-Za-z]*")){
            Log.d("User", first);
            Log.d("User","bad first name");
            msg = "name can only be letters";
            this.name.setText("");
            return false;
        }
        else if (!last.matches("[A-Za-z]*")){
            Log.d("User", "bad last name");
            msg = "name can only be letters";
            this.name.setText("");
            return false;
        }else
            return true;
    }

    /**
     * much like the above method this method just validates the email that was entered
     * @param email
     * @return
     */
    public boolean validateEmail(String email){
        if (email.length() == 0){
            msg = "email cannot be blank";
            Log.d("User", "blank email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            msg = "invalid email";
            Log.d("User", "invalid email");
            this.email.setText("");
            return false;
        }else
            return true;
    }

    /**
     * this method validate the password, making sure it is at least 6 characters
     * @param password
     * @return
     */
    public boolean validatePassword(String password){
        if (password.length() == 0){
            msg = "password can not be blank";
            Log.d("User", "password blank");
            return false;
        }
        else if (!(password.trim().length() > 5)){
            msg = "password must be at least 6 characters";
            Log.d("User", "password too short");
            this.password.setText("");
            return false;
        }else
            return true;
    }

    /**
     * this method makes sure the username is not blank and does not contain spaces
     * @param username
     * @return
     */
    public boolean validateUsername(String username){
        if (username.length() == 0){
            msg = "username cannot be blank";
            Log.d("User", "username blank");
            return false;
        }else if (username.contains(" ")){
            msg = "username can not contain spaces";
            Log.d("User", "username with spaces");
            this.username.setText("");
            return false;
        }else
        return true;
    }
}