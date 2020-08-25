package com.demotoothie.bridge;

import android.content.Context;
import android.content.Intent;
import tv.danmaku.ijk.media.widget.IjkVideoView;

import com.demotoothie.application.Config;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import static tv.danmaku.ijk.media.widget.IRenderView.AR_ASPECT_FIT_PARENT;

public class RNTCameraView extends IjkVideoView {

    private static final int VIDEO_VIEW_RENDER = IjkVideoView.RENDER_TEXTURE_VIEW;

    public RNTCameraView(Context context) {
        super(context);
    }

    @ReactMethod
    private void connect() {
        this.setRender(VIDEO_VIEW_RENDER);
        this.setAspectRatio(AR_ASPECT_FIT_PARENT);
        this.setVideoPath(Config.PREVIEW_ADDRESS);
        this.start();
    }
}
