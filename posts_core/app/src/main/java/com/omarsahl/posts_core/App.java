package com.omarsahl.posts_core;

import android.app.Application;

import io.flutter.facade.Flutter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Flutter.startInitialization(getApplicationContext());
    }
}
