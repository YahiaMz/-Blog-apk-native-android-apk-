package com.example.blog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Create_Post extends AppCompatActivity {

    private EditText desc ;
    private CardView image;
    private ImageButton back;
    private Button submit;
    private ImageView imageViewPost;
    private SharedPreferences sharedP;
    private ProgressDialog dialog;

    private final  static  int CHANGE_IMAGE = 65;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        init();
    }


    void init( ){
        this.desc = findViewById(R.id.postcreate_desc);
        this.image = findViewById(R.id.postcreate_change_image);
        this.back = findViewById(R.id.backBtn);
        this.submit = findViewById(R.id.create_postBtn);
        this.imageViewPost = findViewById(R.id.create_post_image);
        this.dialog = new ProgressDialog(getApplicationContext());
        this.imageViewPost.setImageURI(getIntent().getData());



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
                if(validateDesc_pass()) createPost();
                else Toast.makeText(getApplicationContext(), "Description is required", Toast.LENGTH_SHORT).show();
            }
        });


    }
    boolean validateDesc_pass( ) {
        return  !(desc.getText().toString().isEmpty());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHANGE_IMAGE && resultCode==RESULT_OK) {
            this.imageViewPost.setImageURI(data.getData());

        }

    }
    private  void createPost( ) {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest  request = new StringRequest(
                Request.Method.POST, Constants.create_Post, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                     onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
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
                parms.put("photo" , bitmapToString(bitmap));
              return  parms;
            }
        };



        queue.add(request);

    }

    private  String bitmapToString(Bitmap bitmap){
        BitmapDrawable drawable = (BitmapDrawable) imageViewPost.getDrawable();
        bitmap = drawable.getBitmap();

        System.out.println(bitmap);

        if(bitmap==null) return "";


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);

        return temp;

    }

}