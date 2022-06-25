package Adapters;

import static com.example.blog.R.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.Comment_Activity;
import com.example.blog.Constants;
import com.example.blog.EditPost_Activity;
import com.example.blog.Models.PostModel;
import com.example.blog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHoler> {

    private ArrayList<PostModel> posts;
    //private int currentPostId = -1;
    private boolean like = false;
    private SharedPreferences sharedPreferences;

    public PostsAdapter(ArrayList<PostModel> posts, Context context) {
        this.posts = posts;
        this.context = context;

    }

    private Context context;

    @NonNull
    @Override
    public PostHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout.post_layout, parent, false);

        return new PostHoler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostHoler holder, @SuppressLint("RecyclerView") int position) {


        sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        PostModel post = posts.get(position);


        Picasso.get().load(Constants.Url + "/storage/posts/" + post.getPhoto()).into(holder.imagePost);
        Picasso.get().load(Constants.Url + "/storage/profiles/" + post.getUser().getPhoto()).into(holder.userImage);
        holder.userName.setText(post.getUser().getName() + " " + post.getUser().getLastName());
        holder.date.setText(post.getDate());
        holder.description.setText(post.getDesc());
        holder.comment_count.setText("View all " + post.getComment() + " comment");
        holder.like_count.setText(post.getLike() + " likes");

        holder.like.setImageResource(post.isSelflike() ? drawable.like_true : drawable.like_false);


        holder.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnOption);
                popupMenu.inflate(menu.post_popupmenu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case id.edit:
                                goToEditPage(post.getDesc(), position);
                                break;
                            case id.delete:
                                deletePost(post);
                                break;
                        }

                        return false;

                    }
                });
            }
        });

        holder.coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_Comment_page(post.getId() , position);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("like btn clicked");
                //        currentPostId = post.getId();
                like(position);
                holder.like.setImageResource(post.isSelflike() ? drawable.like_false : drawable.like_true);
            }
        });


        System.out.println("user id  : " + sharedPreferences.getInt("id", 48946));
        if (post.getUser().getId() == sharedPreferences.getInt("id", -1)) {
            System.out.println(" user id  = post.userid ");
            holder.btnOption.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    class PostHoler extends RecyclerView.ViewHolder {

        private TextView userName, date, description, like_count, comment_count;
        private CircleImageView userImage;
        private ImageView imagePost;
        private ImageButton btnOption, like, coment;
        private CardView postCardView;


        public PostHoler(@NonNull View itemView) {
            super(itemView);


            userImage = itemView.findViewById(id.post_userPhoto);
            userName = itemView.findViewById(id.post_userName);
            date = itemView.findViewById(id.post_date);
            description = itemView.findViewById(id.post_desc);
            like_count = itemView.findViewById(id.post_LIke_count);
            comment_count = itemView.findViewById(id.post_commentCount);
            imagePost = itemView.findViewById(id.post_image);
            like = itemView.findViewById(id.post_like);
            coment = itemView.findViewById(id.post_comment);
            btnOption = itemView.findViewById(id.post_btnOption);
            postCardView = itemView.findViewById(id.postCardView);


            this.btnOption.setVisibility(View.GONE);

        }
    }

    private void like(int posistion) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.like, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("like " + response);
                posts.get(posistion).setSelflike(!posts.get(posistion).isSelflike());
                posts.get(posistion).setLike(Integer.parseInt(response.toString()) > 0 ? posts.get(posistion).getLike() + 1 : posts.get(posistion).getLike() - 1);
                notifyItemChanged(posistion);
                notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("like : " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("post_id", posts.get(posistion).getId() + "");
                //System.out.println("like : Sending data to " + postId);

                sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
                map.put("token", sharedPreferences.getString("token", ""));
                return map;
            }
        };
        queue.add(request);

    }

    void goToEditPage(String desc, int pos) {
        Intent intent = new Intent(context, EditPost_Activity.class);
        intent.putExtra("position", pos);
        intent.putExtra("desc", desc);
        context.startActivity(intent);
    }


    private void deletePost(PostModel post) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.DELETE_POST, new Response.Listener<String>() {

            //  private  SharedPreferences sharedPreferences;

            @Override
            public void onResponse(String response) {


                posts.remove(post);
                //  notifyItemRemoved(position);

                //   Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                System.out.println("DELETE : " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);

                header.put("Authorization", "Bearer " + sharedPreferences.getString("token", ""));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("id", post.getId() + "");
                return parms;
            }
        };


        queue.add(request);
    }
    private void goto_Comment_page ( int postID , int postPos) {
        Intent intent = new Intent(context , Comment_Activity.class);
        intent.putExtra("post_id" , postID);
        intent.putExtra("postPosition" , postPos);
        context.startActivity(intent);
    }

}
