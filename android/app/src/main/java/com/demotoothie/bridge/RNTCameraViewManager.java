package com.demotoothie.bridge;

import android.content.Context;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import tv.danmaku.ijk.media.widget.IjkVideoView;

public class RNTCameraViewManager extends SimpleViewManager<RNTCameraView> {
    @NonNull
    @Override
    public String getName() {
        return "RNTCameraView";
    }

    @NonNull
    @Override
    protected RNTCameraView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNTCameraView(reactContext);
    }
}
