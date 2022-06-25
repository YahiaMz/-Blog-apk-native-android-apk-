package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.blog.AuthActivity;
import com.example.blog.Constants;
import com.example.blog.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    AnimationDrawable animationDrawable;
    LinearLayout layout;
    private View view;
    private SharedPreferences preferences;
    private CircleImageView uesr_image;
    private TextView userName;
    private TextView userEmail;
    private ImageButton logoutImgBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(com.example.blog.R.layout.account_layout, container, false);

        init();


        return view;
    }

    private void init() {
        setVar_values();

       layout = this.view.findViewById(R.id.testingAnimatedDrawable);

        animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);;

        animationDrawable.start();


        ///////////////////////////////////////////

        this.logoutImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }

    private void setVar_values() {
        preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);

        uesr_image = this.view.findViewById(R.id.account_imageUser);
        userName = this.view.findViewById(R.id.account_layout_user_name);
        userEmail = this.view.findViewById(R.id.account_layout_user_email);

        this.logoutImgBtn = view.findViewById(R.id.account_layout_logoutImgBtn);


        Picasso.get().load(Constants.Url + "/storage/profiles/" + preferences.getString("photo", "")).into(uesr_image);
        this.userName.setText(preferences.getString("name", " ") + " " + preferences.getString("last_Name", " "));
        this.userEmail.setText(preferences.getString("email", " "));


    }


    private void logout() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Constants.logout, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Log out Successfully", Toast.LENGTH_SHORT).show();
                preferences.edit().clear();
                startActivity(new Intent(getContext()  , AuthActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("Authorization", "Bearer " +preferences.getString("token" , ""));
                return  parms;
            }
        };


        queue.add(request);
    }
}