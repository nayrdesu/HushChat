package com.ansoft.chatapp;

import android.app.Application;

import com.parse.Parse;
public class YouChatApplication extends Application {

    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
        super.onCreate();
    }
}
