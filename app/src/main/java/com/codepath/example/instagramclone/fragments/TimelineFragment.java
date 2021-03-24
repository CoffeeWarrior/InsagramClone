package com.codepath.example.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.codepath.example.instagramclone.Post;
import com.codepath.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {
    private RecyclerView rvTimeline;
    private LinearLayoutManager linearLayoutManager;
    private PostsAdapter postsAdapter;
    public static final String TAG = "TimelineFragment";
    private SwipeRefreshLayout swipeContainer;


    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
        setUpRecyclerView(view);

        Log.i(TAG, "onViewCreated: " + new Date());

        updateTimeline();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsAdapter.clear();
                updateTimeline();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Paused Timeline fragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "resumed timeline fragment");
    }

    public void updateTimeline(){
        Log.i(TAG, "timeline updated");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.setLimit(10);
        Log.i(TAG, "last date: " + postsAdapter.getLastPostDate());
        query.whereLessThan("createdAt", postsAdapter.getLastPostDate());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if(posts.size() == 0){
                    return;
                }
                swipeContainer.setRefreshing(false);
                int items = postsAdapter.getItemCount();
                for(Post post:posts){
                    try {
                        post.setUsername(post.getUser().fetchIfNeeded().getUsername());
                        postsAdapter.addPost(post);
                        postsAdapter.notifyItemInserted(++items);
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    Log.i(TAG, "username: " + post.getUsername());
                }
            }
        });
    }

    public void setUpRecyclerView(View view){
        rvTimeline = view.findViewById(R.id.rvTimeline);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTimeline.setLayoutManager(linearLayoutManager);
        postsAdapter = new PostsAdapter();
        rvTimeline.setAdapter(postsAdapter);
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager){

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                updateTimeline();
            }
        });
    }

}