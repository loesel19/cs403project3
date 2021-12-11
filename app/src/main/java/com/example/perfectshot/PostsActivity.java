package com.example.perfectshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    Button btnMakePost;
    User user;

    PostsAdapter postsAdapter;
    ArrayList<JSONObject> posts;
    ArrayList<JSONObject> ratings;

    RecyclerView recPosts;


    //After making a new post, update the ArrayList to include the new post
    @Override
    protected void onPostResume() {
        super.onPostResume();
        postsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        btnMakePost = findViewById(R.id.btnMakePost);

        btnMakePost.setOnClickListener(view -> startCreate(view));
        recPosts = findViewById(R.id.recPosts);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");

        //This will hold all posts to be displayed in Recyclerview
        //Using an ArrayList because we don't know exactly how many posts to display
        posts = new ArrayList<JSONObject>();

        //We'll set up the Recyclerview before posts are loaded, then fill them in.
        //Displays items in the RecyclerView in a vertical list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recPosts.setLayoutManager(linearLayoutManager);

        postsAdapter = new PostsAdapter(posts, getApplicationContext(), user);
        recPosts.setAdapter(postsAdapter);

        //Get the posts from DB
        //We get the most recent posts, capping the number at 100
        RequestQueue q = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest j = new JsonObjectRequest(Request.Method.GET,
                "https://frozen-reaches-15850.herokuapp.com/get_feed?limit=100",
                null, reply->{
            try {
                JSONArray arr = reply.getJSONArray("posts");
                //Get the posts from JSON reply, store in posts ArrayList
                for (int c = 0; c < reply.getInt("count"); c++) {
                    posts.add(arr.getJSONObject(c));
                }
                //Notify RecyclerView that we have our posts from the DB
                postsAdapter.notifyDataSetChanged();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }, error -> {

        });
        q.add(j);
    }

    //Go to the CreatePost Activity
    public void startCreate(View view){
        Intent i = new Intent(this, CreatePostActivity.class);
        if (user!=null)
            i.putExtra("User",user);
        startActivity(i);
    }

    //Go to the Settings Activity
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
    ArrayList<JSONObject> posts;
    RequestQueue queue;
    Context context;
    User currentUser;

    PostsAdapter(ArrayList<JSONObject> posts , Context context, User user){
        this.posts = posts;
        queue = Volley.newRequestQueue(context);
        this.context = context;
        currentUser = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new ViewHolder(v);
    }

    //Reads posts ArrayList item and maps to post_card layout
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject postInfo = posts.get(position);
        int author = 0;
        try {
            author = postInfo.getInt("author");

            int image_id = postInfo.getInt("image_id");
            String desc = postInfo.getString("description");
            float lon = postInfo.getLong("location_long");
            float lat = postInfo.getLong("location_lat");
            //Create onClick for the button and pass lat,lon,desc
            holder.post_mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MapsActivity.class);
                    i.putExtra("desc", desc);
                    i.putExtra("lat", lat);
                    i.putExtra("lon",lon);
                    context.startActivity(i);

                }
            });
            holder.post_descript.setText(desc);
            holder.post_lat.setText(lat+"");
            holder.post_long.setText(lon+"");

            holder.postid = postInfo.getInt("id");
            holder.userid = currentUser.id;

            //Gets author username from id
            getAuthor(author, holder.post_author);
            getRatingScore(postInfo.getInt("id"), holder.post_rateScore);
            //Load image
            Picasso.get().load("https://frozen-reaches-15850.herokuapp.com/get_image/"+image_id).into(holder.post_img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getRatingScore(int post, TextView target){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_rating?post="+post, null, response ->{
            try {
                //Log.i("KANGASTEST", response.toString());
                double l = response.getDouble("value");
                double dr = Math.round(l*10.0)/10.0;
                target.setText(dr+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->{

        });
        queue.add(r);
    }

    //Gets author username from id
    public void getAuthor(int author, TextView target){
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, "https://frozen-reaches-15850.herokuapp.com/get_user?id="+author, null, response ->{
            try {
                //Log.i("KANGASTEST", response.toString());
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
        return this.posts.size();
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
        public TextView post_rateScore;
        public ImageView post_img;
        int userid = -1;
        int postid = -1;
        public Spinner spinner;
        public Button post_mapBtn;

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
            post_rateScore = (TextView) itemView.findViewById(R.id.post_rateScore);
            post_img = (ImageView) itemView.findViewById(R.id.post_img);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.rating_values, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            RequestQueue q = Volley.newRequestQueue(context);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i>0){

                        JSONObject j = new JSONObject();
                        try {
                            j.put("rater", userid);
                            j.put("post", postid);
                            j.put("value", i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("EVAN", j.toString());
                        JsonObjectRequest r = new JsonObjectRequest(Request.Method.POST, "https://frozen-reaches-15850.herokuapp.com/new_rating", j, response -> {
                            try {
                                double ll = response.getDouble("new_value");
                                double dr = Math.round(ll*10.0)/10.0;
                                post_rateScore.setText(dr+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {

                        });
                        q.add(r);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            post_mapBtn = (Button) itemView.findViewById(R.id.post_mapBtn);
        }
    }
}