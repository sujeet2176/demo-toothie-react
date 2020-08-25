package com.demotoothie.bridge;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class ControlModule extends ReactContextBaseJavaModule {
    public ControlModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return ControlModule.class.getName();
    }

    @ReactMethod
    public void launchControlModule(){

    }
}
