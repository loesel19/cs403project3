package com.example.perfectshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    Button btnMakePost;
    User user;

    PostsAdapter postsAdapter;
    int [] posts;

    RecyclerView recPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        btnMakePost = findViewById(R.id.btnMakePost);

        btnMakePost.setOnClickListener(view -> startCreate(view));
        recPosts = findViewById(R.id.recPosts);


        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");

        posts = new int[100];

        for (int n = 0; n<100;n++){
            posts[n] = 0;
        }

        postsAdapter = new PostsAdapter(posts, getApplicationContext());
        recPosts.setAdapter(postsAdapter);
    }


    public void startCreate(View view){
        Intent i = new Intent(this, CreatePostActivity.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }


    public void startSettings(View view){
        Intent i = new Intent(this, Settings.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }

    public void backBtn(View v){
        super.finish();
    }
}

class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    int [] posts;
    RequestQueue queue;
    Context context;

    PostsAdapter(int [] posts , Context context){
        this.posts = posts;
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_feed?offset="+position, null, response ->{
            try {
                int author = response.getInt("author");
                int image_id = response.getInt("image_id");
                String desc = response.getString("description");
                float lon = response.getLong("location_long");
                float lat = response.getLong("location_lat");
                holder.post_descript.setText(desc);
                holder.post_lat.setText(lat+"");
                holder.post_long.setText(lon+"");
                getAuthor(author, holder.post_author);

                Picasso.get().load("https://frozen-reaches-15850.herokuapp.com/get_image/"+image_id).into(holder.post_img);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->{

        });
        queue.add(r);
    }

    public void getAuthor(int author, TextView target){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_user?"+author, null, response ->{
            try {
                target.setText(response.getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->{

        });
        queue.add(r);
    }

    @Override
    public int getItemCount() {
        return posts.length;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView post_author;
        public TextView post_lat;
        public TextView post_long;
        public TextView post_descript;
        public RatingBar post_rateStars;
        public ImageView post_img;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            post_author = (TextView) itemView.findViewById(R.id.post_author);
            post_lat = (TextView) itemView.findViewById(R.id.post_lat);
            post_long = (TextView) itemView.findViewById(R.id.post_long);
            post_descript = (TextView) itemView.findViewById(R.id.post_descript);
            post_rateStars = (RatingBar) itemView.findViewById(R.id.post_rateStars);
            post_img = (ImageView) itemView.findViewById(R.id.post_img);
        }
    }
}