package com.codepath.example.instagramclone;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.example.instagramclone.fragments.PostsAdapter;

public class CustomEndlessRecyclerViewScroll extends RecyclerView.OnScrollListener {
    public static final String TAG = "CustomEndlessRecyclerViewScroll";
    private static final int postsLeftBeforeLoading = 5; //when there are 10 posts left in our feed, we want to load more posts
    private boolean loading = true;
    private int previousTotalItemCount = 0;
    private int startingPageIndex = 0;

    LinearLayoutManager layoutManager;


    public CustomEndlessRecyclerViewScroll(LinearLayoutManager linearLayoutManager) {
        layoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        Log.i(TAG, "current item: " + lastVisibleItemPosition);
        if(totalItemCount < previousTotalItemCount){
            //we know that something is wrong with our current list, perhaps swipe to refresh happened.
            // Now our logic below CAN be liable to break b/c it is possible that totalItemCount will not be greater than previous total item count, which will make loading stuck as true & thus unable to load new items
            this.previousTotalItemCount = totalItemCount;
            if(totalItemCount == 0){
                this.loading = true;
            }
        }

        if(loading && (totalItemCount > previousTotalItemCount)){
            Log.i(TAG, "We have finished loading");
            //if loading is true, we check to see if it has finished loading (total item count being greater than previous totalItemCount)
            //if it has finished loading we can set loading to false and update previous totalItemCount to reflect the current amount of items
            previousTotalItemCount = totalItemCount;
            loading = false;
        }

        if(!loading && (lastVisibleItemPosition + postsLeftBeforeLoading) > totalItemCount){
            Log.i(TAG, "we need to start loading, currently at item: " + lastVisibleItemPosition);
            //if you're not loading & you have to be loading (have to be loading is represented by lastvisibleItemposition + postsLeftBeforeLoading being greater than totalitemCount)
            //then start loading -- update previousTotalItemCount
            loading = true;
            loadMore(totalItemCount, recyclerView);
        }

    }

    public void loadMore(int totalItemCount, RecyclerView recyclerView) {
    }
}
