package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.blog.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUP_Fragment extends Fragment {
    private View view;
    private TextView loginTxt;
    private TextInputEditText email , passwod , confirm;
    private TextInputLayout emailLayout , passwordLayout , confirmLayout;
    private Button SignUp_btn;
    private ProgressDialog dialog ;
    public  SignUP_Fragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_layout , container , false);
    init();
        return view;
    }

    public  void init ( ) {
        inialVaribales();
        this.loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout , new Sign_In_fragment()).commit();
            }
        });
        this.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmailValidatore();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.passwod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordValidatore();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmValidatore();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }

        );
SignUp_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        regester();
    }
});


    }

    void  inialVaribales( ) {
        this.loginTxt = this.view.findViewById(R.id.txtSignIn);
        this.email = view.findViewById(R.id.EmailSignUp);
        this.passwod = view.findViewById(R.id.Password_SignUp);
        this.emailLayout = view.findViewById(R.id.EmailTxtLayout_Singup);
        this.passwordLayout = view.findViewById(R.id.passwordTxtLayout_Singup);
        this.confirmLayout = view.findViewById(R.id.confirmTxtLayout_Singup);
        this.confirm = this.view.findViewById(R.id.Confirm_password_SignUp);
        this.SignUp_btn = this.view.findViewById(R.id.regesterBtn);
        this.dialog = new ProgressDialog(getContext());
    }
    private boolean EmailValidatore() {
        boolean e = true;

        if(email.getText().toString().isEmpty() ) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Email is required");
            e = false;
        } else {
            emailLayout.setErrorEnabled(false);
        }
        return e;
    }
    private  boolean passwordValidatore( ) {
        boolean p = true;
        if(passwod.getText().toString().isEmpty()) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Password is required");
            p = false;
        } else if (passwod.getText().toString().length()<8) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Required at least 8");
            p = false;
        } else {
            passwordLayout.setErrorEnabled(false   );

        }
        return p;
    }
    private  boolean confirmValidatore( ) {
        boolean b = true;
        if (this.confirm.getText().toString().isEmpty()) {
            b =false;
            this.confirmLayout.setErrorEnabled(true);
            this.confirmLayout.setError("confirm field is empty");
        } else if( !this.passwod.getText().toString().equals( this.confirm.getText().toString()) ) {
            b = false;
            this.confirmLayout.setErrorEnabled(true);
            this.confirm.setError("confirm is not eqauls to password");
        } else  {
            this.confirmLayout.setErrorEnabled(false);
        }
        return  b;
    }

    public  void regester( ) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
       dialog.setMessage("regestering ");
       dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.regester, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.getBoolean("success")){
                        JSONObject user = jsonObject.getJSONObject("user");

                        SharedPreferences userData = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
                        SharedPreferences.Editor userEditor = userData.edit();
                        userEditor.putString("name" , user.getString("name"));
                        userEditor.putString("last_Name" , user.getString("last_Name"));
                        userEditor.putString("photo" , user.getString("photo"));
                        userEditor.putBoolean("isLogin" , true );
                        userEditor.apply();

                        Toast.makeText(getContext() , "regestred succufully" , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext() , jsonObject.get("message").toString() , Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<String, String>();
                map.put("email" , email.getText().toString().trim());
                map.put("password" , passwod.getText().toString().trim());
                return  map;
            }
        };
        queue.add(request);
    }
}
