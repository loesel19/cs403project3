package com.example.perfectshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class API {

    static RequestQueue requestQueue;
    static Context context;

    public static void init(Context context){
        API.context = context;
        API.requestQueue = Volley.newRequestQueue(context);
    }

    public static void SEND_login(String e, String p, UserCallback callback){

        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/login?email=" + e + "&password="+ p, null, response ->{
            try {
                APIUser user = API.parseUserObject(response);
                callback.SuccessAction(user);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error ->{
            callback.ErrorAction();
        });

        API.requestQueue.add(r);

    }

    public static APIUser parseUserObject(JSONObject response) throws JSONException {
        int id = response.getInt("id");
        String first_name = response.getString("first_name");
        String last_name = response.getString("last_name");
        String username = response.getString("username");
        String password = response.getString("password");
        String email = response.getString("email");
        String created_at = response.getString("created_at");

        APIUser user = new APIUser(id, first_name, last_name, username, password, email, created_at);
        return user;
    }

    public static void SEND_new_user(APIUser user, UserCallback callback){
        JSONObject jo = user.toJson();
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_user", jo, response ->{
            try {
                APIUser u = API.parseUserObject(response);
                callback.SuccessAction(u);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error ->{
            callback.ErrorAction();
        });

        API.requestQueue.add(r);
    }

    public static void SEND_get_rating(int postID, RatingCallback callback){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_rating?post="+postID, null, response ->{
            try {
                callback.SuccessAction(response.getInt("value"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error ->{
            callback.ErrorAction();
        });

        API.requestQueue.add(r);
    }

    public static void SEND_new_rating(APIRating rating, RatingCallback callback){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_user", rating.toJson(), response ->{
            try {
                callback.SuccessAction(response.getInt("new_value"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error ->{
            callback.ErrorAction();
        });

        API.requestQueue.add(r);
    }

    public static void SEND_new_post(APIPost post, Bitmap image_bitmap, PostCallback callback){

        API.postImage(image_bitmap, new ImageCallback() {
            @Override
            public void SuccessAction(int id) {
                post.image_id = id;
                JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_user", post.toJson(), response ->{
                    try {
                        APIPost p = API.parsePostObject(response);
                        callback.SuccessAction(p);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }, error ->{
                    callback.ErrorAction();
                });

                API.requestQueue.add(r);
            }

            @Override
            public void ErrorAction() {

            }
        });
        //image https://stackoverflow.com/questions/20322528/uploading-images-to-server-android

    }

    public static void postImage(Bitmap image_bitmap, ImageCallback callback){
        VolleyMultiPart volleyMultipartRequest = new VolleyMultiPart(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/image_upload",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            callback.SuccessAction(obj.getInt("imageid"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image_bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                params.put("image", new DataPart(imagename + ".png", byteArrayOutputStream.toByteArray()));
                return params;
            }
        };
        API.requestQueue.add(volleyMultipartRequest);
    }

    public static APIPost parsePostObject(JSONObject response) throws JSONException{
        APIPost post = new APIPost(
                response.getInt("id"),
                response.getInt("author"),
                response.getInt("image_id"),
                response.getString("description"),
                response.getLong("location_lat"),
                response.getLong("location_long"),
                response.getString("created_at")
        );
        return post;
    }

    public static void SEND_get_feed(int limit, FeedCallback callback){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_feed?limit="+limit, null, response ->{
            try {
                ArrayList<APIPost> posts = new ArrayList<>();
                int count = response.getInt("count");
                for (int i=0;i<count; i++) {
                    JSONObject jp = response.getJSONArray("posts").getJSONObject(i);
                    posts.add(API.parsePostObject(jp));
                }

                callback.SuccessAction(posts);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error ->{
            callback.ErrorAction();
        });

        API.requestQueue.add(r);
    }

    public static void SEND_get_image(int imageID, ImageView view){
        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        Picasso.with(context).load(imageUri).into(view);
    }
}

interface UserCallback{
    public void SuccessAction(APIUser user);
    public void ErrorAction();
}
interface RatingCallback{
    public void SuccessAction(int value);
    public void ErrorAction();
}
interface PostCallback{
    public void SuccessAction(APIPost post);
    public void ErrorAction();
}
interface ImageCallback{
    public void SuccessAction(int id);
    public void ErrorAction();
}
interface FeedCallback{
    public void SuccessAction(ArrayList<APIPost> posts);
    public void ErrorAction();
}
interface LoadImageCallback{
    public void Action();
}


class APIUser{
    int id;
    String first_name;
    String last_name;
    String username;
    String password;
    String email;
    String created_at;

    public APIUser(int id, String first_name, String last_name, String username, String password, String email, String created_at) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    public JSONObject toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put("first_name", this.first_name);
            j.put("last_name", this.last_name);
            j.put("username", this.username);
            j.put("password", this.password);
            j.put("email", this.email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }
}

class APIRating{
    int id;
    int rater;
    int ratie;
    int post;
    float value;

    public APIRating(int id, int rater, int ratie, int post, Long value) {
        this.id = id;
        this.rater = rater;
        this.ratie = ratie;
        this.post = post;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", rater=" + rater +
                ", ratie=" + ratie +
                ", post=" + post +
                ", value=" + value +
                '}';
    }

    public JSONObject toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put("rater", this.rater);
            j.put("ratie", this.ratie);
            j.put("post", this.post);
            j.put("value", this.value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }
}

class APIPost{
    int id;
    int author;
    int image_id;
    String description;
    Long location_lat;
    Long location_long;
    String created_at;

    public APIPost(int id, int author, int image_id, String description, Long location_lat, Long location_long, String created_at) {
        this.id = id;
        this.author = author;
        this.image_id = image_id;
        this.description = description;
        this.location_lat = location_lat;
        this.location_long = location_long;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "APIPost{" +
                "id=" + id +
                ", author=" + author +
                ", image_id=" + image_id +
                ", description='" + description + '\'' +
                ", locatino_lat=" + location_lat +
                ", location_long=" + location_long +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    public JSONObject toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put("author", this.author);
            j.put("image_id", this.image_id);
            j.put("description", this.description);
            j.put("location_lat", this.location_lat);
            j.put("location_long", this.location_long);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }
}