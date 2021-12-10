package com.example.perfectshot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UserDAO {
RequestQueue queue;
String postURL;
String requestURL;
JSONObject postData;
    User user;
    boolean responseType;
    private final String createdString = "add-user";
    private final String getString = "get-user";
    private final String containsString = "contains-user";
    Context context;

    /**
     * this constructor takes in the context that the DAO was instantiated at and passes that context
     * to our volley request queue, and we also set up the request and post URLs
     * @param context
     */
    public UserDAO(Context context){

        postURL= "https://frozen-reaches-15850.herokuapp.com/new_user";
        queue = Volley.newRequestQueue(context);
        this.context = context;

    }

    public boolean add(User user) throws AuthFailureError, ExecutionException, InterruptedException {
        postData = new JSONObject();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        try {
            postData.put("first_name", user.getFirst_name());
            postData.put("last_name", user.getLast_name());
            postData.put("username", user.getUsername());
            postData.put("password", user.getPassword());
            postData.put("email",user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postURL, postData, future,future);
        queue.add(jsonObjectRequest);
        try{
            JSONObject response = future.get(10, TimeUnit.SECONDS);
            Log.d("User", "add user: " + response.toString());
            return true;
        }catch(Exception e){
            Log.d("User", "add user: " +  e.toString());
            return false;
        }

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

    public void get(String email, String password) throws ExecutionException, InterruptedException, JSONException {

        requestURL = requestURL + "email=" + email.trim() + "&password=" + password.trim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User u = null;
                try {
                    u = new User(response.getString("username"), response.getString("password"),
                            response.getString("first_name"), response.getString("last_name"),
                            response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("User", "get user: " + response.toString());
                    Intent i = new Intent(getString);
                    i.putExtra("code",200);
                    i.putExtra("user", u.toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.d("User", "get user: " + error.toString());
                Intent i = new Intent(getString);

            }
        });
        queue.add(jsonObjectRequest);



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
    public void contains(User user) throws ExecutionException, InterruptedException {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL + "email=" + user.getEmail() + "&password=" + user.getPassword(),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
Log.d("User", "Contains " + user.toString());
Intent i = new Intent(containsString);
i.putExtra("contains", true);
LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("User", "Contains " + error.toString());
                Intent i = new Intent(containsString);
                i.putExtra("contains", false);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        });
        queue.add(jsonObjectRequest);
    }
}
