package com.demotoothie.bridge;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demotoothie.application.Config;
import com.demotoothie.comm.MessageCenter;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import tv.danmaku.ijk.media.widget.IjkVideoView;

import static tv.danmaku.ijk.media.widget.IRenderView.AR_ASPECT_FIT_PARENT;

public class RNTCameraViewManager extends ViewGroupManager<RNTCameraView> {
    private static final String RNTCameraView = "RNTCameraView";

    ReactApplicationContext reactApplicationContext;
    public RNTCameraViewManager(ReactApplicationContext reactApplicationContext) {
        this.reactApplicationContext = reactApplicationContext;
    }

    @NonNull
    @Override
    public String getName() {
        return RNTCameraView;
    }

    @NonNull
    @Override
    protected RNTCameraView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNTCameraView(reactContext);
    }

    @ReactMethod
    public void connect(RNTCameraView rntCameraView, Promise promise) {
        try {
            if (rntCameraView != null)
                rntCameraView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(RNTCameraView, "Connect called");
                        MessageCenter.getInstance().start();
                        rntCameraView.playVideo();
                        promise.resolve(null);
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
            Log.e(RNTCameraView, "Connect called err " + e);
        }

    }

    @Override
    public void receiveCommand(@NonNull com.demotoothie.bridge.RNTCameraView rntCameraView, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(rntCameraView, commandId, args);
        try {
            Assertions.assertNotNull(rntCameraView);
            Assertions.assertNotNull(args);
            rntCameraView.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(RNTCameraView, "Connect called");
                    MessageCenter.getInstance().start();
                    rntCameraView.playVideo();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(RNTCameraView, "Connect called err " + e);
        }
    }
}
