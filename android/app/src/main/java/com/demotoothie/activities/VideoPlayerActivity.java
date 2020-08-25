package com.demotoothie.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sdkpoc.R;

import tv.danmaku.ijk.media.widget.AndroidMediaController;
import tv.danmaku.ijk.media.widget.IjkMpOptions;
import tv.danmaku.ijk.media.widget.IjkVideoView;

import static tv.danmaku.ijk.media.widget.IRenderView.AR_ASPECT_FIT_PARENT;
import static tv.danmaku.ijk.media.widget.IjkVideoView.RENDER_TEXTURE_VIEW;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = VideoPlayerActivity.class.getSimpleName();

    private String mVideoPath;
    private Uri mVideoUri;

    private AndroidMediaController mMediaController;
    private IjkVideoView mVideoView;
    private TextView mToastTextView;
    private TableLayout mHudView;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mRightDrawer;

    private boolean mBackPressed;

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoTitle", videoTitle);
        return intent;
    }

    public static void intentTo(Context context, String videoPath, String videoTitle) {
        context.startActivity(newIntent(context, videoPath, videoTitle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_player);

        // handle arguments
        mVideoPath = getIntent().getStringExtra("videoPath");

//        if (!TextUtils.isEmpty(mVideoPath)) {
//            new RecentMediaStorage(this).saveUrlAsync(mVideoPath);
//        }

        // init UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);

        String videoTitle = getIntent().getStringExtra("videoTitle");
        if (actionBar != null)
            actionBar.setTitle(videoTitle);

        mToastTextView = (TextView) findViewById(R.id.toast_text_view);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        // init player
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setRender(RENDER_TEXTURE_VIEW);
        mVideoView.setAspectRatio(AR_ASPECT_FIT_PARENT);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        mVideoView.setOnErrorListener(new IjkVideoView.IVideoView.OnErrorListener() {
            @Override
            public boolean onError(IjkVideoView videoView, int what, int extra) {
//                Toast.makeText(VideoPlayerActivity.this, R.string.VideoView_error_text_unknown, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        applyOptionsToVideoView(mVideoView);

        mHudView.setVisibility(View.GONE);

        // prefer mVideoPath
        if (mVideoPath != null)
            mVideoView.setVideoPath(mVideoPath);
        else if (mVideoUri != null)
            mVideoView.setVideoURI(mVideoUri);
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mVideoView.start();
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
    }

    /* VideoPlayer Options */

    private void applyOptionsToVideoView(IjkVideoView videoView) {
        // default options
        IjkMpOptions options = IjkMpOptions.defaultOptions();
        // custom options
        options.setPlayerOption("mediacodec", 0);
        // JPEG解析方式，默认使用填充方式（即网络数据包丢失，则用上一帧数据补上），可以改为DROP（丢失数据包则丢掉整帧，网络不好不要使用），ORIGIN（原始方式，不要使用）
        options.setPlayerOption("rtp-jpeg-parse-packet-method", IjkMpOptions.RTP_JPEG_PARSE_PACKET_METHOD_ORIGIN);
        // 读图像帧超时时间，单位us。如果在这个时间内接收不到一个完整图像，则断开连接
        options.setPlayerOption("readtimeout", 5000 * 1000);
        // apply options to VideoView
        videoView.setOptions(options);
    }

}
