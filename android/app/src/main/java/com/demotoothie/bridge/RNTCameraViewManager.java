package com.demotoothie.bridge;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demotoothie.R;
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

public class RNTCameraViewManager extends SimpleViewManager<RNTCameraView> {
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
        return new RNTCameraView(reactApplicationContext.getCurrentActivity());
    }

    @ReactProp(name = "on")
    public void setOn(RNTCameraView rntCameraView, boolean on) {
        try {
            Toast.makeText(reactApplicationContext, "Connect src : " + on, Toast.LENGTH_LONG).show();
            Log.d(RNTCameraView, "Connect called " + on);
            if (on) {
                MessageCenter.getInstance().start();
                rntCameraView.playVideo();
//                rntCameraView.setBackgroundColor(reactApplicationContext.getResources().getColor(R.color.ijk_transparent_dark));
            } else {
//                rntCameraView.setBackgroundColor(reactApplicationContext.getResources().getColor(R.color.colorAccent));
                MessageCenter.getInstance().stop();
//                rntCameraView.stopVideo();
            }
            Log.d(RNTCameraView,"val "+rntCameraView);
        } catch (
                Exception e) {
            e.printStackTrace();
            Log.e(RNTCameraView, "Connect called err " + e);
        }

    }
}
