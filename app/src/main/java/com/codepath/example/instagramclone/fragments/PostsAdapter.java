package com.codepath.example.instagramclone.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.example.instagramclone.Post;
import com.codepath.example.instagramclone.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.widget.ParseImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static final String TAG = "PostsAdapter";
    private List<Post> posts = new ArrayList<Post>();
    public PostsAdapter(){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.post, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);
    }

    public void addPost(Post post){ this.posts.add(post); }

    public Date getLastPostDate(){
        //get the most recent post date or the current date
        if(posts.size() > 0){
            return this.posts.get(posts.size() - 1).getCreatedAt();
        }
        return new Date();
    }

    public void clear(){
        this.posts.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername, tvDescription;
        public ParseImageView ivImagePost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImagePost = itemView.findViewById(R.id.ivImagePost);
        }

        public void bind(Post post){
            tvUsername.setText(post.getUsername());
            tvDescription.setText(post.getDescription());
//            Glide.with(itemView.getContext()).load(post.getImage().getUrl()).into(ivImagePost);
            ivImagePost.setParseFile(post.getImage());
            ivImagePost.loadInBackground();
        }
    }

}
