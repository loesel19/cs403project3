package com.example.perfectshot;

import android.widget.Toast;

import java.io.Serializable;

/**
 * this is a simple User object with username, password and a status
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private boolean loggedIn;
    public int id=-1;

    public User(String username, String password, String first_name, String last_name, String email){
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public User(String username, String password, String first_name, String last_name, String email, int id){
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.id = id;
    }
    public void setStatus(boolean status){
        this.loggedIn = status;
    }
    public String getUsername(){
        return this.username;
    }
    public boolean getStatus(){
        return this.loggedIn;
    }
    public String getPassword(){
        return this.password;
    }
    public String getFirst_name(){
        return this.first_name;
    }
    public String getLast_name(){
        return this.last_name;
    }
    public String getEmail(){
        return this.email;
    }

    public String toString(){
        String s = first_name.trim() + " " + last_name.trim() + " " + email.trim() + " " + username.trim() + " " + password.trim() + " " + loggedIn;
        if (id>0)
            s += " " + id;
        return  s;
    }

}
