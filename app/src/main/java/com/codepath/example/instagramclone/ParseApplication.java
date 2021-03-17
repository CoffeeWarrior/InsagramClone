package com.codepath.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //register parse class
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dQYWkH08M6XDOAJyUxCmAn1eUALN0lopL4Ewphvr")
                .clientKey("y3L6qpoQTdXkJPRi5Dff0ldfXZRLIi8Iy8Voy3W1")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
