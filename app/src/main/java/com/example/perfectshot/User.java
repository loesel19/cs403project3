package com.example.perfectshot;

/**
 * this is a simple User object with username, password and a status
 */
public class User {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private boolean loggedIn;


    public User(String username, String password, String first_name, String last_name, String email){
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
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
        return first_name + " " + last_name + " " + email + " " + username + " " + loggedIn;
    }
}
