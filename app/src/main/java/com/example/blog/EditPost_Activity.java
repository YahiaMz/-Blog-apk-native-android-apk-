package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.Models.PostModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandleInfo;
import java.util.HashMap;
import java.util.Map;

import Fragments.HomeFragment;

public class EditPost_Activity extends AppCompatActivity {
    private EditText desc ;
    private CardView image;
    private ImageButton back;
    private Button submit;
    private Bitmap bitmap;
    private int CHANGE_IMAGE = 8;
    private ImageView imageViewPost;
    private SharedPreferences sharedP;
   private   PostModel post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post2);
        post = HomeFragment.posts.get(getIntent().getExtras().getInt("position"));
        init();


            //The key argument here must match that used in the other activity
         this.desc.setText(post.getDesc());
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , CHANGE_IMAGE);
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!desc.getText().toString().isEmpty()){
                    edit(post.getId());
                }
            }
        });
     //   Picasso.get().load(Constants.Url + "/storage/posts/" + post.getPhoto()).into(holder.imagePost);
             System.out.println( "photo " +  post.getPhoto());
        Picasso.get().load(Constants.Url+"/storage/posts/"+post.getPhoto()).into(imageViewPost);


    }
    void init( ){
        this.desc = findViewById(R.id.postEdit_desc);
        this.image = findViewById(R.id.postEdit_change_image);
        this.back = findViewById(R.id.postEdit_backBtn);
        this.submit = findViewById(R.id.edit_postBtn);
        this.imageViewPost = findViewById(R.id.create_post_image);


    //    this.imageViewPost.setImageURI(getIntent().getData());
    }

 void edit ( int id){
     RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
     StringRequest request = new StringRequest(Request.Method.POST, Constants.EDIT_POST, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             try {
                 JSONObject object = new JSONObject(response);
             if(object.getBoolean("success")) {

                 System.out.println("updating new infor");

              post.setDesc(desc.getText().toString());
              HomeFragment.posts.set(getIntent().getExtras().getInt("position") , post);
              HomeFragment.recyclerView.getAdapter().notifyItemChanged(getIntent().getExtras().getInt("position"));
              HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();

             }



             } catch (JSONException e) {
                 e.printStackTrace();
             }


             onBackPressed();

         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     }
     ){
         @Override
         public Map<String, String> getHeaders() throws AuthFailureError {
             Map<String , String> headers= new HashMap<String , String>();
             sharedP = getSharedPreferences("userData" , MODE_PRIVATE);
             headers.put("Authorization" , "Bearer "+ sharedP.getString("token" ,""));
             return headers;
         }

         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String , String> parms= new HashMap<String , String>();
             sharedP = getSharedPreferences("userData" , MODE_PRIVATE);
             parms.put("desc" , desc.getText().toString());
           //  if( bitmap != null) parms.put("photo" , bitmapToString(bitmap));
             parms.put("user_id" , sharedP.getInt("id" , -6) +"");
             parms.put("id" , id+"");


             return  parms;
         }
     };


     queue.add(request);

 }
    private  String bitmapToString(Bitmap bitmap){
        BitmapDrawable drawable = (BitmapDrawable) imageViewPost.getDrawable();
        bitmap = drawable.getBitmap();

        System.out.println("bitmap : " +  bitmap.toString());

        if(bitmap==null) return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);

        return temp;

    }
}

