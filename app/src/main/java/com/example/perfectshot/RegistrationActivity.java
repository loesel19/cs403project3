package com.example.perfectshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.perfectshot.ui.login.LoginActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //assign views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        dao = new UserDAO();


    }

    /**
     * this activity will call the activity to validate the inputs, if invalid it displays a message
     * with what is invalid, then it checks to see if the username is already in use, and if it is not
     * it will add the user to the database and take them to the log in activity.
     * @param v
     */
    public void register(View v){
        if (validateInputs()){
            Log.d("User", "user added");
            if (dao.contains(user)){
                Toast.makeText(this, "Username already in use", Toast.LENGTH_LONG);
                username.setText("");
            }else{
                if (dao.add(user)){
                    Toast.makeText(this, "Registration complete", Toast.LENGTH_LONG);
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(this, "something went wrong please try again", Toast.LENGTH_LONG);
                }
            }
        }else{
            Toast.makeText(this, msg, Toast.LENGTH_LONG);
            Log.d("User", "user was not added");
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

        if (!(validateName(sName)) && validateEmail(sEmail) && validatePassword(sPassword) && validateUsername(sUsername))
            return false;
        else {
            user = new User(sUsername, sPassword, sName.split(" ")[0], sName.split(" ")[1], sEmail);
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
        else if (!first.matches("[A-Za-z]")){
            Log.d("User","bad first name");
            msg = "name can only be letters";
            return false;
        }
        else if (!last.matches("[A-Za-z]")){
            Log.d("User", "bad last name");
            msg = "name can only be letters";
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
            return false;
        }else
        return true;
    }
}