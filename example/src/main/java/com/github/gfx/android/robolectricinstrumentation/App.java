package com.github.gfx.android.robolectricinstrumentation;

import com.jakewharton.threetenabp.AndroidThreeTen;

import android.app.Application;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
