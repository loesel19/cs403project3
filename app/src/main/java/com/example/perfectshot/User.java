package com.example.perfectshot;

/**
 * this is a simple User object with username, password and a status
 */
public class User {
    private String username;
    private String password;
    private boolean loggedIn;


    public User(String username, String password){
        this.username = username;
        this.password = password;
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
}
