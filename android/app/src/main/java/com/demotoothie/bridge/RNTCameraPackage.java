package com.demotoothie.bridge;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RNTCameraPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        //We import the module file here
        //        nativeModules.add(new RNTCameraViewManager(reactContext));
        return Collections.emptyList();
    }


    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
      List<ViewManager> viewManagers = new ArrayList<>();
      viewManagers.add(new RNTCameraViewManager(reactContext));
      return viewManagers;
    }

}
