package com.demotoothie.bridge;

import android.Manifest;
import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import tv.danmaku.ijk.media.widget.IjkMpOptions;
import tv.danmaku.ijk.media.widget.IjkVideoView;

import com.demotoothie.R;
import com.demotoothie.Utilities;
import com.demotoothie.application.Config;
import com.demotoothie.application.Settings;
import com.demotoothie.comm.MessageCenter;
import com.demotoothie.comm.TCPMessage;
import com.demotoothie.eventbus.BusProvider;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.demotoothie.application.Config.RECONNECTION_INTERVAL;
import static tv.danmaku.ijk.media.widget.IRenderView.AR_ASPECT_FIT_PARENT;

public class RNTCameraView extends IjkVideoView {


    private static final String RNTCameraView = "RNTCameraView";

    // 渲染视图，不需要更改
    private static final int VIDEO_VIEW_RENDER = IjkVideoView.RENDER_TEXTURE_VIEW;
    // 拉伸方式，根据需要选择等比例拉伸或者全屏拉伸等
    private static int VIDEO_VIEW_ASPECT = AR_ASPECT_FIT_PARENT;

    // 右侧按键
//    private ViewGroup mRightMenuBar;
//    private ImageButton mTakePhotoButton;
//    private ImageButton mRecordVideoButton;
//    private ImageButton mReviewButton;
//    private ImageButton mRotateScreenButton;
//    private ImageButton mResolutionButton;

    private String mVideoPath;
//    private IjkVideoView mIjkVideoView;
//    private TableLayout mHudView;
//    private FrameLayout videoViewParent;

    private boolean recording = false;

    // 控制台界面
//    private ImageView mBackgroundView;
//    private ProgressBar mProgressBar;
//    private Chronometer mChronometer;

    // 剩余空间监控
    private com.example.sdkpoc.buildwin.common.widget.freespacemonitor.FreeSpaceMonitor mFreeSpaceMonitor;

    // 其他
    private static String videoFilePath = null; // 用以保存录制视频文件的路径
    private SoundPool mSoundPool;

    // Debug
    // 打开右侧设置按钮，按住陀螺仪校准按钮，再长按打开右侧的设置按钮，即可打开帧数等信息。关闭方法重复操作一遍。
    private boolean touchDebug = false;

    // 720P
    private boolean b720p;

    // 视图旋转角度
    private int mRotationDegree = 0;

    private List<ResolutionModel> mResolutionList;

    // 权限
    private String msgPrefix;
    private String msgComma;
    private String msgDeniedExtStorage;

    // 用于兼容旧版本，如果指令来自新API，则置true
    private boolean fromNewApi;
    private Activity mActivity;

    public RNTCameraView(Activity context) {
        super(context);

        mActivity = context;

        // 订阅事件
       /* new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {*/
                msgPrefix = mActivity.getResources().getString(R.string.permission_denied_prefix);
                msgComma = mActivity.getResources().getString(R.string.permission_denied_comma);
                msgDeniedExtStorage = mActivity.getResources().getString(R.string.permission_denied_external_storage);
                BusProvider.getBus().register(this);
                MessageCenter.getInstance().start();
                RNTCameraView.this.setBackgroundColor(context.getResources().getColor(R.color.Orchid));
                b720p = Settings.getInstance(context).getParameterForPhoto720p();
                VIDEO_VIEW_ASPECT = AR_ASPECT_FIT_PARENT;
                mVideoPath = Config.PREVIEW_ADDRESS;
                if (!initVideoView(RNTCameraView.this, mVideoPath)) {
                    Log.e(RNTCameraView, "initVideoView fail");
                }

                // 载入声音资源
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SoundPool.Builder builder = new SoundPool.Builder();
                    builder.setMaxStreams(1);

                    AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
                    attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);

                    builder.setAudioAttributes(attrBuilder.build());
                    mSoundPool = builder.build();
                } else {
                    mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
                }
                mSoundPool.load(mActivity, R.raw.shutter, 1);

            /*}
        });*/

//        mIjkVideoView = this;


        /**
         * Right Menu Bar
         * 右侧菜单按键
         */
//        mRightMenuBar = (ViewGroup) findViewById(R.id.control_panel_right_menubar);

        /**
         * 背景图像
         */
//        mBackgroundView = (ImageView) findViewById(R.id.control_panel_backgroundView);

        /**
         * 进度条
         */
//        mProgressBar = (ProgressBar) findViewById(R.id.control_panel_progressBar);

        /**
         * 录像计时器
         */
//        mChronometer = (Chronometer) findViewById(R.id.control_panel_chronometer);

        // To expose to react native
//        videoViewParent = findViewById(R.id.video_view_parent);

        // Photo&Video 720P
        // 720P填充，VGA按比例拉伸
//        if (b720p)
//            VIDEO_VIEW_ASPECT = AR_MATCH_PARENT;
//        else

        // handle arguments

        // init UI
//        mHudView = (TableLayout) findViewById(R.id.hud_view);
//        mHudView.setVisibility(View.GONE);

        // init player
//        this = (IjkVideoView) findViewById(R.id.video_view);


        /**
         * Take Photo Button
         * 截图按钮
         */
        /*mTakePhotoButton = (ImageButton) findViewById(R.id.control_panel_take_photo_button);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(1);
            }
        });*/

        /**
         * Record Video Button
         * 录像按钮
         */
        /*mRecordVideoButton = (ImageButton) findViewById(R.id.control_panel_record_video_button);
        mRecordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo();
            }
        });*/

        /**
         * Replay Button
         * 查看按钮
         */
        /*mReviewButton = (ImageButton) findViewById(R.id.control_panel_review_button);
        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start replaying
                Intent i = new Intent(context, ReviewActivity.class);
                startActivity(i);
                // Activity slide from left
                overridePendingTransition(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                );
            }
        });
*/
        /**
         * Rotate Screen Button
         * 旋转屏幕按钮
         */
        /*mRotateScreenButton = (ImageButton) findViewById(R.id.control_panel_rotate_screen_button);
        mRotateScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rotate the screen
//                this.setRotation180(!this.isRotation180());
                // 每次旋转90度，顺时针
                mRotationDegree += 90;
                mRotationDegree %= 360;
                RNTCameraView.this.setVideoRotation(mRotationDegree);
            }
        });*/

        /**
         * Select video resolution button
         * 选择分辨率按钮
         */
        /*mResolutionButton = (ImageButton) findViewById(R.id.control_panel_resolution_button);
        mResolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 录像时无法改变分辨率
                if (recording) {
                    Toast.makeText(context, R.string.control_panel_cannot_change_resolution, Toast.LENGTH_SHORT).show();
                    return;
                }
                showVideoResolution();
            }
        });*/


        /**
         * 初始化控件显示
         */
//        mChronometer.setVisibility(View.GONE);

        // check permissions
       /* Dexter.withActivity(mContext)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(allPermissionsListener)
                .check();*/
    }

    /* IjkPlayer */

    private boolean initVideoView(IjkVideoView videoView, String videoPath) {
        if (videoView == null)
            return false;

        // init player
//        videoView.setHudView(mHudView);

        // 准备开始预览回调
        videoView.setOnPreparedListener(mPlayerPreparedListener);
        // 发生错误回调
        videoView.setOnErrorListener(mPlayerErrorListener);
        // 播放完成后
        videoView.setOnCompletionListener(mPlayerCompletionListener);
        // 接收RTCP数据
        videoView.setOnReceivedRtcpSrDataListener(mReceivedRtcpSrDataListener);
        // 拍照回调
        videoView.setOnTookPictureListener(mTookPictureListener);
        // 录像回调
        videoView.setOnRecordVideoListener(mRecordVideoListener);

        // set options before setVideoPath
        applyOptionsToVideoView(videoView);

        // prefer mVideoPath
        if (videoPath != null)
            videoView.setVideoPath(videoPath);
        else {
            Log.e(RNTCameraView, "Null Data Source\n");
            return false;
        }

        return true;
    }

    private void applyOptionsToVideoView(IjkVideoView videoView) {
        // default options
        IjkMpOptions options = IjkMpOptions.defaultOptions();
        // custom options
        options.setPlayerOption("mediacodec", 0);
        // JPEG解析方式，默认使用填充方式（即网络数据包丢失，则用上一帧数据补上），可以改为DROP（丢失数据包则丢掉整帧，网络不好不要使用），ORIGIN（原始方式，不要使用）
        options.setPlayerOption("rtp-jpeg-parse-packet-method", IjkMpOptions.RTP_JPEG_PARSE_PACKET_METHOD_DROP);
        // 读图像帧超时时间，单位us。如果在这个时间内接收不到一个完整图像，则断开连接
        options.setPlayerOption("readtimeout", 5000 * 1000);
        // Image type (PREFERRED_IMAGE_TYPE_*)
        options.setPlayerOption("preferred-image-type", IjkMpOptions.PREFERRED_IMAGE_TYPE_JPEG);
        // 默认时，如果源格式和目标格式是相同的话，不进行转码，直接保存
        // 但是，有些USB Sensor格式保存成jpg的时候有些问题，需要强制转码
        // 如果是航拍的话不使用
        options.setPlayerOption("image-force-transcoding", 1);
        // Image quality, available for lossy format (min and max are both from 1 to 51, 0 < min <= max, smaller is better, default is 2 and 31)
        options.setPlayerOption("image-quality-min", 2);
        options.setPlayerOption("image-quality-max", 20);
        // video
        options.setPlayerOption("preferred-video-type", IjkMpOptions.PREFERRED_VIDEO_TYPE_H264);
        options.setPlayerOption("video-force-transcoding", 1);
        options.setPlayerOption("mjpeg-pix-fmt", IjkMpOptions.MJPEG_PIX_FMT_YUVJ422P);
        // 强制使用帧率，如果FFMPEG能猜准的话，似乎不起作用
        options.setPlayerOption("video-force-framerate-enable", 1); // 使能
        options.setPlayerOption("video-force-framerate", 30);       // 设置帧率
        // video quality, for MJPEG and MPEG4
        options.setPlayerOption("video-quality-min", 2);
        options.setPlayerOption("video-quality-max", 20);
        // x264 preset, tune and profile, for H264
        options.setPlayerOption("x264-option-preset", IjkMpOptions.X264_PRESET_ULTRAFAST);
        options.setPlayerOption("x264-option-tune", IjkMpOptions.X264_TUNE_ZEROLATENCY);
        options.setPlayerOption("x264-option-profile", IjkMpOptions.X264_PROFILE_MAIN);
        options.setPlayerOption("x264-params", "crf=20");
        // 检测到小错误就停止当前帧解码，避免图像异常
        options.setCodecOption("err_detect", "explode");
        // apply options to VideoView
        videoView.setOptions(options);
    }

    private IjkVideoView.IVideoView.OnPreparedListener mPlayerPreparedListener
            = new IjkVideoView.IVideoView.OnPreparedListener() {
        @Override
        public void onPrepared(IjkVideoView videoView) {
            onStartPlayback();
            // 连接上视频后获取Sensor分辨率列表
            MessageCenter.getInstance().seneMessageGetSensorResolution();
        }
    };

    private IjkVideoView.IVideoView.OnErrorListener mPlayerErrorListener
            = new IjkVideoView.IVideoView.OnErrorListener() {
        @Override
        public boolean onError(IjkVideoView videoView, int what, int extra) {
//            mResolutionButton.setVisibility(View.GONE);
            stopAndRestartPlayback();
            return true;
        }
    };

    private IjkVideoView.IVideoView.OnCompletionListener mPlayerCompletionListener
            = new IjkVideoView.IVideoView.OnCompletionListener() {
        @Override
        public void onCompletion(IjkVideoView videoView) {
            // 当成错误处理
//            mResolutionButton.setVisibility(View.GONE);
            stopAndRestartPlayback();
        }
    };

    // 兼容旧版API使用
    private IjkVideoView.IVideoView.OnReceivedRtcpSrDataListener mReceivedRtcpSrDataListener
            = new IjkVideoView.IVideoView.OnReceivedRtcpSrDataListener() {
        @Override
        public void onReceivedRtcpSrData(IjkVideoView videoView, byte[] data) {
//            Log.d(TAG, new String(data) + Arrays.toString(data));

            if (checkIfIsValidHwActionCommand(data)) {
                byte commandClass = data[4];    // index

                switch (commandClass) {
                    case HW_ACTION_CLASS_TAKE_PHOTO:
                    case HW_ACTION_CLASS_RECORD_VIDEO: {
                        byte command = data[6];         // data
                        doHwAction(commandClass, command);
                        break;
                    }
                    case HW_ACTION_CLASS_RESOLUTION: {
                        int dataLen = data.length;
                        int num = data[5];
                        if ((dataLen - 6 >= num * 4) && (dataLen - 6) % 4 == 0) {
                            List<ResolutionModel> resolutionList = new ArrayList<>();
                            int index = 6;
                            for (int i = 0; i < num; i++) {
                                int w = (data[index] & 0xFF) << 8 | (data[index + 1] & 0xFF);
                                int h = (data[index + 2] & 0xFF) << 8 | (data[index + 3] & 0xFF);

                                // 在这里过滤分辨率
                                if (!isSupportedResolution(w, h)) {
                                    index += 4;
                                    continue;
                                }

                                ResolutionModel resolutionModel = new ResolutionModel(i, w, h);
                                resolutionList.add(resolutionModel);

                                index += 4;
                            }
                            synchronized (mActivity) {
                                mResolutionList = resolutionList;
                            }
                            // resort list
                            Collections.sort(mResolutionList, new Comparator<ResolutionModel>() {
                                @Override
                                public int compare(ResolutionModel r1, ResolutionModel r2) {
                                    int d = r2.getWidth() - r1.getWidth();
                                    if (d == 0) {
                                        return r2.getHeight() - r1.getHeight();
                                    }
                                    return d;
                                }
                            });
                            // 显示更改分辨率按键
//                            mResolutionButton.setVisibility(View.VISIBLE);
                            // 来自旧API
                            fromNewApi = false;
                        }
                        break;
                    }
                }
                // Workaround, 因为改变分辨率后origin点还在原来地方，造成显示有问题，所以放在这里
                // 重新Layout
                RNTCameraView.this.setVideoRotation(mRotationDegree);
            }
        }
    };

    private IjkVideoView.IVideoView.OnTookPictureListener mTookPictureListener
            = new IjkVideoView.IVideoView.OnTookPictureListener() {
        @Override
        public void onTookPicture(IjkVideoView videoView, int resultCode, String fileName) {
            String toastText = mActivity.getResources().getString(R.string.control_panel_alert_save_photo_fail);
            if (resultCode == 1) {
                // 播放咔嚓声
                mSoundPool.play(1, 1, 1, 0, 0, 1);
            } else if (resultCode == 0 && fileName != null) {
                File file = new File(fileName);
                if (file.exists()) {
                    mediaScan(file);
                    // Show toast
                    toastText = mActivity.getResources().getString(R.string.control_panel_alert_save_photo_success) + fileName;
                }
                Toast.makeText(mActivity, toastText, Toast.LENGTH_SHORT).show();
            } else if (resultCode < 0) {
                Toast.makeText(mActivity, toastText, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private IjkVideoView.IVideoView.OnRecordVideoListener mRecordVideoListener
            = new IjkVideoView.IVideoView.OnRecordVideoListener() {
        @Override
        public void onRecordVideo(IjkVideoView videoView, final int resultCode, final String fileName) {
            Handler handler = new Handler(mActivity.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String noteText = null;
                    if (resultCode < 0) {
                        // 停止监控剩余空间
                        if (mFreeSpaceMonitor != null)
                            mFreeSpaceMonitor.stop();

                        recording = false;
                        noteText = mActivity.getResources().getString(R.string.control_panel_alert_write_video_file_error);
                        Toast.makeText(
                                mActivity,
                                noteText,
                                Toast.LENGTH_SHORT
                        ).show();
//                        mRecordVideoButton.setImageResource(R.mipmap.con_video);
                        // 隐藏录像计时器
                        showChronometer(false);
                    } else if (resultCode == 0) {
                        recording = true;
                        // 开启录像计时
                        showChronometer(true);
//                        mRecordVideoButton.setImageResource(R.mipmap.con_video_h);
                        // 开始监控剩余空间
                        mFreeSpaceMonitor.setListener(new com.example.sdkpoc.buildwin.common.widget.freespacemonitor.FreeSpaceMonitor.FreeSpaceCheckerListener() {
                            @Override
                            public void onExceed() {
                                // 如果剩余空间低于阈值，停止录像
                                if (recording)
                                    RNTCameraView.this.stopRecordVideo();
                            }
                        });
                        mFreeSpaceMonitor.start();
                    } else {
                        // 停止监控剩余空间
                        if (mFreeSpaceMonitor != null)
                            mFreeSpaceMonitor.stop();

                        // Scan file to media library
                        File file = new File(fileName);
                        mediaScan(file);

                        noteText = mActivity.getResources().getString(R.string.control_panel_alert_record_video_success);
                        Toast.makeText(
                                mActivity,
                                noteText + fileName,
                                Toast.LENGTH_SHORT
                        ).show();
//                        mRecordVideoButton.setImageResource(R.mipmap.con_video);
                        // 隐藏录像计时器
                        showChronometer(false);

                        // set flag
                        recording = false;
                    }
                }
            });
        }
    };

    /**
     * 开始播放预览视频
     */
    public void playVideo() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RNTCameraView.this.setRender(VIDEO_VIEW_RENDER);
                RNTCameraView.this.setAspectRatio(VIDEO_VIEW_ASPECT);
                RNTCameraView.this.setVideoPath(mVideoPath);
                RNTCameraView.this.start();
                Log.d(RNTCameraView.class.getName(), "playVideo");
            }
        });

    }

    /**
     * 停止播放预览视频
     */
    private void stopVideo() {
        this.stopPlayback();
        this.release(true);
        //        mProgressBar.setVisibility(View.VISIBLE);
    }

        /*@Override
        public void onBackPressed() {
            super.onBackPressed();
            finish();
            // Activity slide from left
            overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );
        }

        @Override
        protected void onStop() {
            super.onStop();

            stopVideo();

            mBackgroundView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onResume() {
            super.onResume();
            // 启动TCP消息中心
            MessageCenter.getInstance().start();
            // 开启屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            playVideo();
        }

        @Override
        protected void onPause() {
            super.onPause();
            // 断开TCP消息中心
            MessageCenter.getInstance().stop();
            // 关闭屏幕常亮
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // 停止录像
            if (recording)
                this.stopRecordVideo();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            mSoundPool.release();

            Settings.release();
        }*/

    /**
     * 播放开始后执行
     */
    private void onStartPlayback() {
        // 隐藏BackgroundView,ProgressBar
//        mBackgroundView.setVisibility(View.INVISIBLE);
//        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 关闭播放器并重新开始播放
     * 错误发生的时候调用
     */
    private void stopAndRestartPlayback() {
        // 显示BackgroundView,ProgressBar
//        mBackgroundView.setVisibility(View.VISIBLE);
//        mProgressBar.setVisibility(View.VISIBLE);

        this.post(new Runnable() {
            @Override
            public void run() {
                stopVideo();
            }
        });
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                playVideo();
            }
        }, RECONNECTION_INTERVAL);
    }

    /**
     * 扫描添加媒体文件到系统媒体库
     *
     * @param file 媒体文件
     */
    private void mediaScan(File file) {
        MediaScannerConnection.scanFile(mActivity,
                new String[]{file.getAbsolutePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.v("MediaScanWork", "file " + path
                                + " was scanned seccessfully: " + uri);
                    }
                });
    }

    /**
     * 显示或者隐藏Chronometer
     *
     * @param bShow 显示开关
     */
    private void showChronometer(boolean bShow) {
        if (bShow) {
//            mChronometer.setVisibility(View.VISIBLE);
//            mChronometer.setBase(SystemClock.elapsedRealtime());
//            mChronometer.start();
        } else {
//            mChronometer.stop();
//            mChronometer.setVisibility(View.INVISIBLE);
//            mChronometer.setText("");
        }
    }

    /**
     * 拍照
     */
    private void takePhoto(int num) {
        // Take a photo
        String photoFilePath = Utilities.getPhotoDirPath(mActivity);
        String photoFileName = Utilities.getMediaFileName();
        try {
            if (b720p) {
                this.takePicture(photoFilePath, photoFileName, 1280, 720, num);
            } else {
                this.takePicture(photoFilePath, photoFileName, -1, -1, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 录像
     */
    private void recordVideo() {
        if (recording) {
            this.stopRecordVideo();
        } else {
            mFreeSpaceMonitor = new com.example.sdkpoc.buildwin.common.widget.freespacemonitor.FreeSpaceMonitor(mActivity);
            if (mFreeSpaceMonitor.checkFreeSpace()) {
                String videoFilePath = Utilities.getVideoDirPath(mActivity);
                String videoFileName = Utilities.getMediaFileName();
                // Start to record video
                try {
                    this.startRecordVideo(videoFilePath, videoFileName, -1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 提示剩余空间不足
                long threshold = mFreeSpaceMonitor.getThreshold();
                float megabytes = (float) threshold / (1024 * 1024);
                String toastString = mActivity.getResources().getString(R.string.control_panel_insufficient_storage_alert, megabytes);
                Toast.makeText(mActivity, toastString, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 弹出分辨率选择列表
     */
    private void showVideoResolution() {
        List<String> titles = new ArrayList<>();
        int count;
        synchronized (mActivity) {
            count = mResolutionList.size();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    ResolutionModel resolutionModel = mResolutionList.get(i);
                    String title = String.format(Locale.getDefault(), "%dx%d", resolutionModel.getWidth(), resolutionModel.getHeight());
                    titles.add(title);
                }
            }
        }

        if (count > 0) {
            // 弹出选择分辨率
            /*ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                    .setCancelButtonTitle("Cancel")
                    .setOtherButtonTitles(titles.toArray(new String[0]))
                    .setCancelableOnTouchOutside(true)
                    .setListener(mContext)
                    .show();*/
        }
    }

    /* ActionSheet */

    /*@Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        ResolutionModel model = mResolutionList.get(index);

        // Send via RTCP
//        byte[] data = new byte[7];
//        data[0] = HW_ACTION_SIGNATURE_BYTE_4;
//        data[1] = HW_ACTION_SIGNATURE_BYTE_3;
//        data[2] = HW_ACTION_SIGNATURE_BYTE_2;
//        data[3] = HW_ACTION_SIGNATURE_BYTE_1;
//        data[4] = 7;
//        data[5] = HW_ACTION_CLASS_RESOLUTION;
//        data[6] = (byte)model.getIndex();
//        this.sendRtcpRrData(data);

        // 兼容新旧API，调用不同的API
        if (fromNewApi) {
            MessageCenter.getInstance().seneMessageSetSensorResolution((byte) model.getIndex());
        } else {
            // Send via TCP Socket
            BWSocket.getInstance().setResolution(model.getIndex());
            // 旧版API在APP端断开
            stopAndRestartPlayback();
        }
    }*/

    /**
     * 过滤分辨率，目前只需要VGA和720P
     */
    private boolean isSupportedResolution(int width, int height) {
        if (width == 640 && height == 480)
            return true;

        if (width == 1280 && height == 720)
            return true;

        return false;
    }

    /* Hardware Action */

    private static final byte HW_ACTION_SIGNATURE_BYTE_1 = 0x0f;
    private static final byte HW_ACTION_SIGNATURE_BYTE_2 = 0x5a;
    private static final byte HW_ACTION_SIGNATURE_BYTE_3 = 0x1e;
    private static final byte HW_ACTION_SIGNATURE_BYTE_4 = 0x69;

    private static final byte HW_ACTION_CLASS_TAKE_PHOTO = 0x00;
    private static final byte HW_ACTION_COMMAND_TAKE_PHOTO = 0x01;

    private static final byte HW_ACTION_CLASS_RECORD_VIDEO = 0x01;
    private static final byte HW_ACTION_COMMAND_RECORD_VIDEO = 0x01;

    private static final byte HW_ACTION_CLASS_RESOLUTION = 0x02;

    private void doHwAction(byte commandClass, byte command) {
        switch (commandClass) {
            case HW_ACTION_CLASS_TAKE_PHOTO:
                if (command == HW_ACTION_COMMAND_TAKE_PHOTO) {
                    takePhoto(1);
                }
                break;
            case HW_ACTION_CLASS_RECORD_VIDEO:
                if (command == HW_ACTION_COMMAND_RECORD_VIDEO) {
                    recordVideo();
                }
                break;
        }
    }

    /**
     * 检查是否是有效的硬件指令
     */
    private boolean checkIfIsValidHwActionCommand(byte[] data) {
        if (data.length >= 7) {
            if (data[0] == HW_ACTION_SIGNATURE_BYTE_1
                    && data[1] == HW_ACTION_SIGNATURE_BYTE_2
                    && data[2] == HW_ACTION_SIGNATURE_BYTE_3
                    && data[3] == HW_ACTION_SIGNATURE_BYTE_4) { // sign
//                byte commandClass = data[4];    // index
//                byte commandLength = data[5];   // len
//                byte command = data[6];         // data

//                if (data.length == commandLength) {
                return true;
//                }
            }
        }
        return false;
    }


    /**
     * 分辨率
     */
    private final class ResolutionModel {

        public ResolutionModel(int index, int width, int height) {
            mIndex = index;
            mWidth = width;
            mHeight = height;
        }

        public int getIndex() {
            return mIndex;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        private final int mIndex;
        private final int mWidth;
        private final int mHeight;
    }


    /* 新的USB Sensor处理方法 */

    /* Message Event */

    // 兼容新版API使用
    @Subscribe
    public void onReceiveMessage(TCPMessage message) {
        byte messageId = message.getMessageId();
        byte[] content = message.getContent();

        // 如果消息类型是report则调用processReport处理
        // 否则调用processMessage处理消息
        if (messageId == TCPMessage.MSG_ID_REPORT) {
            processReport(content);
        } else {
            processMessage(messageId, content);
        }
    }

    /**
     * 处理设备报告
     *
     * @param content 报告内容
     */
    private void processReport(byte[] content) {
        // 目前一个ID对应的设置值均为1Byte，所以这里固定按照两个字节分离
        if (content != null && content.length > 1) {
            byte messageId = content[0];
            byte[] contentBytes = Arrays.copyOfRange(content, 1, 2);
            processMessage(messageId, contentBytes);

            // 递归调用
            byte[] subContent = Arrays.copyOfRange(content, 2, content.length);
            processReport(subContent);
        }
    }

    /**
     * 处理除设备报告之外的消息
     *
     * @param messageId 消息ID
     * @param content   消息内容
     */
    private void processMessage(byte messageId, byte[] content) {
        switch (messageId) {
            case TCPMessage.MSG_ID_RECORD_VIDEO:
                if (content.length > 0) updateRecordVideo(content[0]);
                break;
            case TCPMessage.MSG_ID_TAKE_PHOTO:
                if (content.length > 0) updateTakePhoto(content[0]);
                break;

            case TCPMessage.MSG_ID_GET_SENSOR_RESOLUTION:
                if (content.length > 0) updateSensorResolutionList(content);
                break;

            default:
//                Log.d(TAG, "Unhandled message " + messageId + " from MessageCenter");
        }
    }

    // Record Video
    private void updateRecordVideo(byte value) {
        switch (value) {
            case TCPMessage.RECORD_VIDEO_START: {
                break;
            }
            case TCPMessage.RECORD_VIDEO_STOP: {
                break;
            }
            // 切换录像状态（开始or停止）
            case TCPMessage.RECORD_VIDEO_TOGGLE: {
                recordVideo();
                break;
            }
        }
    }

    // Take Photo
    private void updateTakePhoto(byte value) {
        // 值为0则拍照
        if (value == 0) {
            takePhoto(1);
        }
    }

    // 新版，更新分辨率列表
    private void updateSensorResolutionList(byte[] data) {
        int dataLen = data.length;
        int num = dataLen / 5;
        if (num > 0) {
            List<ResolutionModel> resolutionList = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < num; i++) {
                int idx = data[index];
                int w = (data[index + 1] & 0xFF) << 8 | (data[index + 2] & 0xFF);
                int h = (data[index + 3] & 0xFF) << 8 | (data[index + 4] & 0xFF);

                index += 5;

                // 在这里过滤分辨率
                if (!isSupportedResolution(w, h)) {
                    continue;
                }

                ResolutionModel resolutionModel = new ResolutionModel(idx, w, h);
                resolutionList.add(resolutionModel);
            }
            synchronized (mActivity) {
                mResolutionList = resolutionList;
            }
            // sort list
            Collections.sort(mResolutionList, new Comparator<ResolutionModel>() {
                @Override
                public int compare(ResolutionModel r1, ResolutionModel r2) {
                    int d = r2.getWidth() - r1.getWidth();
                    if (d == 0) {
                        return r2.getHeight() - r1.getHeight();
                    }
                    return d;
                }
            });
            // 显示更改分辨率按键
//            mResolutionButton.setVisibility(View.VISIBLE);
            // 来自新API
            fromNewApi = true;
        }
    }

    /* Permission */

    private MultiplePermissionsListener allPermissionsListener = new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (!report.areAllPermissionsGranted()) {
                StringBuilder msg = new StringBuilder();
                List<PermissionDeniedResponse> deniedList = report.getDeniedPermissionResponses();

                for (PermissionDeniedResponse response : deniedList) {
                    if (msg.length() != 0)
                        msg.append(msgComma);

                    switch (response.getPermissionName()) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            msg.append(msgDeniedExtStorage);
                            break;
                    }
                }
                msg.insert(0, msgPrefix);
                Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

        }
    };

    @ReactProp(name = "src")
    public void connect(RNTCameraView rntCameraView, String src) {
        try {
            Toast.makeText(mActivity, "Connect method toast", Toast.LENGTH_LONG).show();
            Log.d(RNTCameraView, "Connect called");
            MessageCenter.getInstance().start();
            rntCameraView.playVideo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(RNTCameraView, "Connect called err " + e);
        }

    }


}
