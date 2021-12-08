package com.example.perfectshot;

import android.net.Uri;

public class Post {
    Uri postImage;
    User author;
    String postDescription;
    int postID, postRating;
    float latLocation, longLocation;

    public Post() {
    }

    public Post(Uri postImage, String postDescription, int postRating, float latLocation, float longLocation) {
        this.postImage = postImage;
        this.postDescription = postDescription;
        this.postRating = postRating;
        this.latLocation = latLocation;
        this.longLocation = longLocation;
    }

    public Uri getPostImage() {return postImage; }
    public String getPostDescription() {return postDescription; }
    public int getPostRating() { return postRating; }
    public User getAuthor() { return author; }
    public float getLatLocation() { return latLocation; }
    public float getLongLocation() { return longLocation; }

    @Override
    public String toString() {
        return "Post{" +
                "postImage=" + postImage +
                ", author=" + author +
                ", postDescription='" + postDescription + '\'' +
                ", postID=" + postID +
                ", postRating=" + postRating +
                ", latLocation=" + latLocation +
                ", longLocation=" + longLocation +
                '}';
    }
}
