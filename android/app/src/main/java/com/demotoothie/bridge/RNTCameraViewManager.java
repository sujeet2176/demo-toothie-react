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
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;
import java.util.Objects;

import tv.danmaku.ijk.media.widget.IjkVideoView;

import static tv.danmaku.ijk.media.widget.IRenderView.AR_ASPECT_FIT_PARENT;

public class RNTCameraViewManager extends SimpleViewManager<RNTCameraView> {
    private static final String RNTCameraView = "RNTCameraView";

    ReactApplicationContext reactApplicationContext;
    private RNTCameraView mRntCameraView;

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
        if (mRntCameraView == null) {
            mRntCameraView = new RNTCameraView(reactApplicationContext);
        }
        mRntCameraView.init();
        return mRntCameraView;
    }

    @ReactProp(name = "isConnected")
    public void setIsConnected(RNTCameraView rntCameraView, boolean isConnected) {
        try {
            Log.d(RNTCameraView, "Connect called " + isConnected);
            if (isConnected) {
                MessageCenter.getInstance().start();
                rntCameraView.playVideo();
            } else {
                MessageCenter.getInstance().stop();
                rntCameraView.stopVideo();
            }
            Log.d(RNTCameraView, "val " + rntCameraView);
        } catch (
                Exception e) {
            e.printStackTrace();
            Log.e(RNTCameraView, "Connect called err " + e);
        }

    }

    @ReactProp(name = "click")
    public void clickPic(RNTCameraView rntCameraView, int click) {
        if (click != 0) {
            Objects.requireNonNull(reactApplicationContext.getCurrentActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rntCameraView.takePhoto(click);

                }
            });
        }

    }

    @ReactProp(name = "isRecording")
    public void recordVideo(RNTCameraView rntCameraView, Boolean isRecording) {
        Toast.makeText(reactApplicationContext, "isRecording : " + isRecording, Toast.LENGTH_LONG).show();
        Objects.requireNonNull(reactApplicationContext.getCurrentActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRecording != null) {
                    if (isRecording) {
                        rntCameraView.recordVideo();
                    } else {
                        rntCameraView.stopRecording();
                    }
                }

            }
        });

    }

    @ReactProp(name = "isRotate")
    public void setIsRotate(RNTCameraView rntCameraView, Boolean isRotate) {
        try {
            Toast.makeText(reactApplicationContext, "setIsRotate : " + isRotate, Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(reactApplicationContext.getCurrentActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isRotate != null) {
                        rntCameraView.rotateVideo();
                    }

                }
            });


        } catch (
                Exception e) {
            e.printStackTrace();
            Log.e(RNTCameraView, "Connect called err " + e);
        }

    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {

        return MapBuilder.builder()
                .put(
                        "onClickPic",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onClickPic")))
                .put(
                        "onRecordVideo",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onRecordVideo")))
                .build();

    }


}
