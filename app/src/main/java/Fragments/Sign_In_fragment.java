package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.Constants;
import com.example.blog.HOME_acivity;
import com.example.blog.Models.PostModel;
import com.example.blog.Models.UserModel;
import com.example.blog.OnBoardActivity;
import com.example.blog.userInformationActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.PostsAdapter;

public class Sign_In_fragment extends Fragment {
    private  View view;
    private TextInputLayout textInputLayout_Email , textInputLayoutPassword;
    private TextInputEditText emailTxtInput , passwordTxtEdit;
    private Button sing_In_Btn;
    private TextView regester;
    private ProgressDialog dialog;
    private    SharedPreferences userData;

    public  Sign_In_fragment( ){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(com.example.blog.R.layout.sign_in_layout , container , false);

        SharedPreferences sharedPreferences  = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
        init( );
        emailTxtInput.setText(sharedPreferences.getString("email" , ""));

        return  view;

    }































    private void init() {

        this.sing_In_Btn = this.view.findViewById(com.example.blog.R.id.singIn_Btn);
         this.textInputLayout_Email = this.view.findViewById(com.example.blog.R.id.emailTxtLayout);
         this.textInputLayoutPassword = this.view.findViewById(com.example.blog.R.id.passwordTxtLayout);
        this.emailTxtInput = this.view.findViewById(com.example.blog.R.id.EmailSignIn);
        this.passwordTxtEdit = this.view.findViewById(com.example.blog.R.id.passwordSignIn);
        this.regester = this.view.findViewById(com.example.blog.R.id.txtRegester);
          this.dialog = new ProgressDialog(getContext());


        regester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(com.example.blog.R.id.frameLayout , new SignUP_Fragment()).commit();
            }
        });
        sing_In_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 login();
            }
        });
        this.emailTxtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   emailValidate();
                }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        this.passwordTxtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordValidate();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private boolean emailValidate( ){
        boolean e = true;

        if(emailTxtInput.getText().toString().isEmpty() ) {
            textInputLayout_Email.setErrorEnabled(true);
            textInputLayout_Email.setError("Email is required");
             e = false;
        } else {
            textInputLayout_Email.setErrorEnabled(false);
        }
        return e;
    }

    private boolean passwordValidate( ){

        boolean p = true;
        if(passwordTxtEdit.getText().toString().isEmpty()) {
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError("Password is required");
            p = false;
        } else if (passwordTxtEdit.getText().toString().length()<8) {
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError("Required at least 8");
            p = false;
        } else {
            textInputLayoutPassword.setErrorEnabled(false   );

        }
        return p;
    }



























      private  void login( ) {
          RequestQueue queue = Volley.newRequestQueue(getContext());
          dialog.setMessage("logging ..");
          dialog.show();
          StringRequest request = new StringRequest(Request.Method.POST, Constants.login, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {

                  try {
                      JSONObject jsonObject = new JSONObject(response);
                      if( jsonObject.getBoolean("success" )){
                          JSONObject user = jsonObject.getJSONObject("user");

                      userData = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
                          SharedPreferences.Editor userEditor = userData.edit();
                          userEditor.putInt("id" , user.getInt("id"));
                          userEditor.putString("token" , jsonObject.getString("token"));
                          userEditor.putString("photo" , user.getString("photo"));
                          userEditor.putString("name" , user.getString("name"));
                          userEditor.putString("last_Name" , user.getString("last_Name"));
                          userEditor.putString("email" , user.getString("email"));
                          userEditor.putBoolean("isLogin" , true );
                          userEditor.apply();
                          Toast.makeText(getContext() , "login Success" , Toast.LENGTH_SHORT).show();




                          if( user.getString("name").isEmpty() || user.getString("last_Name").isEmpty()) {
                                startActivity(new Intent(getContext(), userInformationActivity.class));
                          } else {
                              startActivity(new Intent(getContext(), HOME_acivity.class));
                              //getPosts();
                          }
                      } else {
                          Toast.makeText(getContext(), "Eamil or passwod Wrong ", Toast.LENGTH_SHORT).show();
                      }
                      dialog.dismiss();
                  } catch (JSONException e) {
                      e.printStackTrace();
                      Toast.makeText(getContext() , "fucking err" , Toast.LENGTH_SHORT).show();
                      dialog.dismiss();
                  }
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  dialog.dismiss();
                  Toast.makeText(getContext() , "Email or password Wrong " , Toast.LENGTH_SHORT).show();

              }
          }){
              @Override
              protected Map<String, String> getParams() throws AuthFailureError {
                  Map<String , String> map = new HashMap<String, String>();
                  map.put("email" , emailTxtInput.getText().toString().trim());
                  map.put("password" , passwordTxtEdit.getText().toString().trim());

                  return  map;
              }
          };
          queue.add(request);
      }















      ////////////////////////////////////////////
      /*private void getPosts() {




          RequestQueue queue = Volley.newRequestQueue(getContext());
          StringRequest request = new StringRequest(Request.Method.GET, Constants.Posts, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  try {
                      System.out.println(response);

                      JSONObject jsonObject = new JSONObject(response);

                      if (jsonObject.getBoolean("success")) {
                          JSONArray array = jsonObject.getJSONArray("posts");
                          for (int x = 0; x < array.length(); x++) {
                              PostModel model = new PostModel(
                                      array.getJSONObject(x).getInt("LikesCount"),
                                      array.getJSONObject(x).getInt("commnetsCount"),
                                      array.getJSONObject(x).getString("desc"),
                                      array.getJSONObject(x).getString("created_at"),
                                      array.getJSONObject(x).getString("photo"),
                                      new UserModel(
                                              array.getJSONObject(x).getJSONObject("user").getInt("id"),
                                              array.getJSONObject(x).getJSONObject("user").getString("name"),
                                              array.getJSONObject(x).getJSONObject("user").getString("last_Name"),
                                              array.getJSONObject(x).getJSONObject("user").getString("photo")
                                      ),
                                      array.getJSONObject(x).getBoolean("selfLike")

                              );


                          }
                      }

                      Toast.makeText(getContext(), "ALl DOne", Toast.LENGTH_SHORT).show();

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }



              }
          },
                  new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          System.out.println("Err : "+ error.toString());

                      }
                  }) {

              @Override
              protected Map<String, String> getParams() throws AuthFailureError {

                  HashMap<String, String> map = new HashMap<>();

                  map.put("token",userData.getString("token", ""));

                  return map;
              }
          };


          queue.add(request);


      }*/


}























