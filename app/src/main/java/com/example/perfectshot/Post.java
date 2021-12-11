package com.example.perfectshot;


import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    int postImage;
    int author;
    String postDescription;
    int postID;
    float latLocation, longLocation;

    public Post() {
    }

    public Post(int author, int postImage, String postDescription, float latLocation, float longLocation) {
        this.postImage = postImage;
        this.postDescription = postDescription;
        this.latLocation = latLocation;
        this.longLocation = longLocation;
        this.author = author;
    }

    public int getPostImage() {return postImage; }
    public String getPostDescription() {return postDescription; }
    public int getAuthor() { return author; }
    public float getLatLocation() { return latLocation; }
    public float getLongLocation() { return longLocation; }

    @Override
    public String toString() {
        return "Post{" +
                "postImage=" + postImage +
                ", author=" + author +
                ", postDescription='" + postDescription + '\'' +
                ", postID=" + postID +
                ", latLocation=" + latLocation +
                ", longLocation=" + longLocation +
                '}';
    }

    public JSONObject toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put("image_id", postImage);
            j.put("author", author);
            j.put("description", postDescription);
            j.put("location_long", longLocation);
            j.put("location_lat", latLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }
}
