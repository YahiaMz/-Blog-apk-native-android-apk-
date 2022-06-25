package Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.blog.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.PostsAdapter;


public class HomeFragment extends Fragment {
    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<PostModel> posts;
    private static SwipeRefreshLayout swipeLayout;
    private static PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        init();

        return view;
    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);

        this.recyclerView = view.findViewById(R.id.recycler_view);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeLayout = view.findViewById(R.id.homeSwipe_layout);

        this.toolbar = view.findViewById(R.id.tool_bar);
        ((HOME_acivity) getContext()).setSupportActionBar(this.toolbar);
        getPosts();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });
    }

    private void getPosts() {
        this.posts = new ArrayList<>();
        swipeLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Constants.Posts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);

                    if (jsonObject.getBoolean("success")) {
                        JSONArray array = jsonObject.getJSONArray("posts");
                        for (int x = 0; x < array.length(); x++) {
                            JSONObject postData = array.getJSONObject(x);

                            PostModel model = new PostModel(


                                    postData.getInt("LikesCount"),
                                    postData.getInt("commnetsCount"),
                                    postData.getString("desc"),
                                    postData.getString("created_at"),
                                    postData.getString("photo"),
                                    new UserModel(
                                            postData.getJSONObject("user").getInt("id"),
                                            postData.getJSONObject("user").getString("name"),
                                            postData.getJSONObject("user").getString("last_Name"),
                                            array.getJSONObject(x).getJSONObject("user").getString("photo")
                                    ),
                                    postData.getBoolean("selfLike")
                            );
                            model.setId(array.getJSONObject(x).getInt("id"));
                            posts.add(model);

                            postsAdapter = new PostsAdapter(posts , getContext());
                            recyclerView.setAdapter(postsAdapter);
                            swipeLayout.setRefreshing(false);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeLayout.setRefreshing(false);
                }



            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                sharedPreferences = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
                System.out.println("token : "+sharedPreferences.getString("token" ,"token") );
                Map<String , String> headers = new HashMap<>();
                headers.put("Authorization" , "Bearer "+sharedPreferences.getString("token" , "token expired"));
           return  headers;
             }

           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("token",sharedPreferences.getString("token", "token"));
                return map;
            }*/
        };


        queue.add(request);



    }
    @Override
    public void onResume() {

        super.onResume();
        this.onCreate(null);
    }



}
