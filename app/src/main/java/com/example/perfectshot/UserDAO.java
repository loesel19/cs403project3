package com.example.perfectshot;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDAO {
RequestQueue queue;
String postURL;
String requestURL;
JSONObject postData;
    User user;
    boolean responseType;

    /**
     * this constructor takes in the context that the DAO was instantiated at and passes that context
     * to our volley request queue, and we also set up the request and post URLs
     * @param context
     */
    public UserDAO(Context context){
        requestURL = "https://frozen-reaches-15850.herokuapp.com/login?"; //add username=password=
        postURL= "https://frozen-reaches-15850.herokuapp.com/new_user";
        queue = Volley.newRequestQueue(context);

    }

    public boolean add(User user) throws AuthFailureError {
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
                Log.d("User", response.toString());
                responseType = true;
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("User", error.toString());
                responseType = false;
            }
        });
queue.add(jsonObjectRequest);
return responseType;
    }

    public boolean delete(String username){
        if (true){
            return true;
        }else{
            return false;
        }
    }

    public boolean delete(User user){
        if (true){
            return true;
        }else{
            return false;
        }
    }

    public User get(String email, String password){

        requestURL = requestURL + "email=" + email.trim() + "&password=" + password;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user = new User(response.getString("username"), response.getString("password"),
                            response.getString("first_name"), response.getString("lasat_name"),
                            response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.d("User", error.toString());
            user = null;
            }
        });
        queue.add(jsonObjectRequest);
        return user;
    }
    public boolean updateName(User user, String newName){
        if (true){
            return true;
        }else{
            return false;
        }
    }
    public boolean updateName(String username, String newName){
        if (true){
            return true;
        }else{
            return false;
        }
    }
    public boolean contains(User user){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL + "username=" + user.getEmail() + "&password=" + user.getPassword(),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
Log.d("User", "Contains " + user.toString());
responseType = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("User", error.toString());
                responseType = false;
            }
        });
        queue.add(jsonObjectRequest);
        return responseType;
    }
}
