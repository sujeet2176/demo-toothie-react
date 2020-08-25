package com.demotoothie.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class GoSkyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Init Fresco
        Fresco.initialize(this);
    }
}
