package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.Models.Comment;
import com.example.blog.Models.PostModel;
import com.example.blog.Models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.MethodHandleInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CommentAdapter;
import Fragments.HomeFragment;

public class Comment_Activity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    private EditText commentEditTxt;
    private ImageButton cancelBtn, submitComentBTN , backBtn;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter comment_Adapter ;
    public static ArrayList<Comment> comments_Array ;
    private SharedPreferences userSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
        getComments();
        onClickListener();


    }

    private void onClickListener() {
        this.submitComentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( commentEditTxt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Comment is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    addComment(getIntent().getExtras().getInt("post_id"));
                }
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentEditTxt.setText("");
            }
        });
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
    }

    private void getComments() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Constants.COMMENTS_OF_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    if (resp.getBoolean("success")) {

                        JSONArray commentsJSONarray = resp.getJSONArray("comments");


                        UserModel userModel ;


                        for (int x = 0; x < commentsJSONarray.length(); x++) {
                            Comment comment = new Comment();
                            JSONObject commentJsonObj = commentsJSONarray.getJSONObject(x);

                            comment.setId(commentJsonObj.getInt("id"));
                            comment.setDate(commentJsonObj.getString("created_at"));
                            comment.setComment(commentJsonObj.getString("comment"));

                            JSONObject userJsonObj = commentJsonObj.getJSONObject("user");

                            userModel = new UserModel(userJsonObj.getInt("id"), userJsonObj.getString("name"), userJsonObj.getString("last_Name"), userJsonObj.getString("photo"));
                            comment.setUser(userModel);
                            comments_Array.add(comment);
                        }



                        comment_Adapter = new CommentAdapter(comments_Array);
                       // recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(comment_Adapter);
                    }
                } catch (JSONException e) {
                    System.out.println("err from json ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Authorization" , "Bearer "+ getSharedPreferences("userData" , MODE_PRIVATE).getString("token" , ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String , String> parms = new HashMap<String,String>();
                parms.put("post_id" , getIntent().getExtras().getInt("post_id")+"");

                return parms;
            }
        };
        queue.add(request);
    }

    private void init() {
        this.userSP = getSharedPreferences("userData" , MODE_PRIVATE);
        this.recyclerView = findViewById(R.id.comment_recyclerView);
        this.commentEditTxt = findViewById(R.id.comment_comment_editT);
        this.cancelBtn = findViewById(R.id.comment_cancel);
        this.submitComentBTN = findViewById(R.id.comment_submit);
        this.backBtn = findViewById(R.id.comment_backbtn);
        this.comments_Array = new ArrayList<>();
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);

    }

    public  void  addComment( int postId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Constants.NEW_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    if(resp.getBoolean("success")) {



                        commentEditTxt.setText(" ");
                        Toast.makeText(getApplicationContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                        Comment comment = new Comment();
                        comment.setUser(new UserModel(userSP.getInt("id" ,-2) , userSP.getString("name" ,"") , userSP.getString("last_Name" ,"" ) , userSP.getString("photo" , "")));

                        JSONObject commentJSONobject = resp.getJSONObject("comment");
                        comment.setComment(commentJSONobject.getString("comment"));
                        comment.setId(commentJSONobject.getInt("id"));
                        comment.setDate(commentJSONobject.getString("created_at"));



                        PostModel postModel = HomeFragment.posts.get(getIntent().getExtras().getInt("postPosition"));
                        postModel.setComment(postModel.getComment()+1);
                        HomeFragment.posts.set(getIntent().getExtras().getInt("postPosition") , postModel);
                        HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();




                        comments_Array.add(comment);
                        recyclerView.getAdapter().notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    System.out.println("adding comment err");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String > headers = new HashMap<>();
                headers.put("Authorization" , "Bearer " + getSharedPreferences("userData" , MODE_PRIVATE).getString("token" , " "));
                return  headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> parms = new HashMap<>();

                parms.put("post_id" , postId+"");
                parms.put("comment" , commentEditTxt.getText().toString());

                return parms;
            }
        };


        queue.add(request);

    }
}