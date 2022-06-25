package Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.blog.Models.Comment;
import com.example.blog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<Comment> commentArrayList ;
    private SharedPreferences shareP;
    private  Context context;
    public CommentAdapter(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }


    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View view_inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_template, null, false);
        CommentHolder commentHolder = new CommentHolder(view_inflated);
        shareP = parent.getContext().getSharedPreferences("userData" , Context.MODE_PRIVATE);
         this.context = parent.getContext();
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, @SuppressLint("RecyclerView") int position) {


        Picasso.get().load(Constants.Url+"/storage/profiles/"+commentArrayList.get(position).getUser().getPhoto()).into(holder.userImage);
        holder.user_Name.setText( commentArrayList.get(position).getUser().getName() + " " + commentArrayList.get(position).getUser().getLastName() );
        holder.comment.setText(commentArrayList.get(position).getComment());
        holder.date.setText(commentArrayList.get(position).getDate());

        if( shareP.getInt("id" , -5) == commentArrayList.get(position).getUser().getId()) {
            holder.comment_option.setVisibility(View.VISIBLE);
        } else  holder.comment_option.setVisibility(View.GONE);

        holder.comment_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu edit_comment_popup = new PopupMenu(context , holder.comment_option);
                edit_comment_popup.inflate(R.menu.edit_comment_menu);
                edit_comment_popup.show();

                edit_comment_popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case  R.id.comment_edit:

                               break;
                            case R.id.comment_delete:
                                   delete_Comment(commentArrayList.get(position).getId() , position);
                                break;
                        }

                        return false;
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImage;
        private TextView user_Name;
        private TextView date;
        private TextView comment;
        private ImageButton comment_option;
        public CommentHolder(@NonNull View itemView) {

            super(itemView);

       this.comment_option = itemView.findViewById(R.id.comment_options);
       this.comment = itemView.findViewById(R.id.comment_template_comment);
       this.date = itemView.findViewById(R.id.comment_date);
       this.user_Name = itemView.findViewById(R.id.comment_nameCommoonter);
       this.userImage = itemView.findViewById(R.id.comment_imageCommenter);
        }
    }



    public  void delete_Comment(int id , int pos) {
  /*      Dialog dialog = new Dialog(context); dialog.setContentView(R.layout.custom_dialog); dialog.setCancelable(true); dialog.show();*/
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.DELETE_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject resp = new JSONObject(response);
                     if(resp.getBoolean("success")) {
                         commentArrayList.remove(pos);

                         notifyItemRemoved(pos);
                         Toast.makeText(context, "Comment Deleted", Toast.LENGTH_SHORT).show();

                     }
                } catch (JSONException e) {
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
                HashMap<String ,String> headers = new HashMap<>();
                headers.put("Authorization" , "Bearer " + shareP.getString("token" , " "));
                return  headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parm = new HashMap<>();
                parm.put("id" , id+"");
                return parm;
            }
        };

 queue.add(request);
    }
}
