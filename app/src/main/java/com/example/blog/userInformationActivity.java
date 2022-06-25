package com.example.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class userInformationActivity extends AppCompatActivity {

    private static final int GALLERY_ADD_PROFILE = 1;
    private CircleImageView profile_image;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private Button submitBtn;
    private TextView setImageProfile;
    private ProgressDialog progressDialog;
    private int PICK_FROM_GALLERY = 1;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initVar();

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (verifyNAME(nameEditText.getText().toString())) {
                    nameEditText.setError("name at least 3 chars");
                } else {
                    nameEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (verifyNAME(lastNameEditText.getText().toString())) {
                    lastNameEditText.setError("last name at least 3 chars");
                } else {
                    lastNameEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
////////////////////////////////////////////

        this.setImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("images/*");
                startActivityForResult(intent,GALLERY_ADD_PROFILE );
            }


        });


        /////////////////////////////::
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyNAME(nameEditText.getText().toString()) && !verifyNAME(lastNameEditText.getText().toString())) {

                    //showToken();
                    submit();

                } else {
                    nameEditText.setError("name required");
                    lastNameEditText.setError("last name required");
                }
            }
        });


        this.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==GALLERY_ADD_PROFILE && resultCode==RESULT_OK) {
            Uri uriImage = data.getData();
            this.profile_image.setImageURI(uriImage);

            try {
                this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , uriImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void initVar() {
        profile_image = findViewById(R.id.image_profile);
        nameEditText = findViewById(R.id.name_edit_txt);
        lastNameEditText = findViewById(R.id.last_name_edit_txt);
        submitBtn = findViewById(R.id.submit);
        setImageProfile = findViewById(R.id.setimageProfile);

    }

    void showToken() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(), sharedPreferences.getString("token", "token"), Toast.LENGTH_SHORT).show();
    }

    private void submit() {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Constants.userInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getApplicationContext() , response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject object = new JSONObject(response);


                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("name", object.getJSONObject("user").getString("name"));
                    editor.putString("last_Name", object.getJSONObject("user").getString("last_Name"));
                    editor.putString("photo", object.getJSONObject("user").getString("photo"));

                    editor.apply();
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext() , HOME_acivity.class));

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();

            }
        }) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", nameEditText.getText().toString().trim());
                params.put("last_Name", lastNameEditText.getText().toString().trim());
                params.put("photo", bitmapToSting(bitmap));
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
                params.put("token", preferences.getString("token", ""));
                return params;

            }
        };


        queue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String bitmapToSting(Bitmap bitmap) {
        if (bitmap == null) return "";
        else {


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
             String s =  Base64.encodeToString(array , Base64.DEFAULT);
            System.out.println("your image is : " + s);
             return s;


          //  Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_SHORT).show();


        }
    }

    private boolean verifyNAME(String txt) {
        return !Pattern.matches("[a-zA-Z]{3,}", txt);
    }


}