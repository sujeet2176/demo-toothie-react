package com.demotoothie.application;

public final class Config {
    public static final String SERVER_IP = "192.168.1.1";
    public static final int SERVER_PORT = 7070;
    // RTSP
    public static final String PREVIEW_ADDRESS = "rtsp://" + SERVER_IP + ":" + SERVER_PORT + "/webcam";
    // 重连等待间隔，单位ms
    public static final int RECONNECTION_INTERVAL = 500;

    // from old file
    public static final String PREFS_NAME = "Prefs";
    public static final int SEND_COMMAND_INTERVAL = 40;     // ms

    /* Comm */

    public static final String TCP_SERVER_HOST          = SERVER_IP;
    public static final int TCP_SERVER_PORT             = 5000;

    public static final int TCP_ALIVE_INTERVAL          = 5000;
    public static final int TCP_RECONNECTION_INTERVAL   = 1; // in seconds

    /* FTP Information */

    public static final String FTP_HOST     = SERVER_IP;
    public static final String FTP_USERNAME = "ftp";
    public static final String FTP_PASSWORD = "ftp";

    /* FTP Path */

    public static final String FTP_ROOT_DIR = "/1/";

    public static final String VIDEO_PATH   = "AVI";
    public static final String IMAGE_PATH   = "photo";

    /* 文件管理 */

    public static final String REMOTE_VIDEO_SUFFIX = ".avi";
    public static final String REMOTE_IMAGE_SUFFIX = ".jpg";

    public static final String LOCAL_VIDEO_SUFFIX = REMOTE_VIDEO_SUFFIX;
    public static final String LOCAL_IMAGE_SUFFIX = REMOTE_IMAGE_SUFFIX;

    /* Web Path */

    public static String VIDEO_THUMB_PATH(String fileName) {
        return "http://" + SERVER_IP + "/" + VIDEO_PATH + "/" + fileName;
    }

    public static String VIDEO_LIVE_PATH(String fileName) {
        return "rtsp://" + SERVER_IP + ":" + SERVER_PORT + "/file/" + VIDEO_PATH + "/" + fileName;
    }

    public static String PHOTO_THUMB_PATH(String fileName) {
        return "http://" + SERVER_IP + "/" + IMAGE_PATH + "/T/" + fileName;
    }

    public static String PHOTO_HTTP_PATH(String fileName) {
        return "http://" + SERVER_IP + "/" + IMAGE_PATH + "/O/" + fileName;
    }

    /* 本地存储路径 */

    // 主目录名
    public static final String HOME_PATH_NAME = "MediaStore";
    // 照片和视频目录名
    public static final String IMAGE_PATH_NAME = "Photos";
    public static final String VIDEO_PATH_NAME = "Movies";


    /* Navigation Params */
    public static final int NAVIGATION_BAR_HEIGHT_LANDSCAPE = 32;
    public static final int NAVIGATION_BAR_HEIGHT_PORTRAIT = 44;
    /* BottomView Params */
    public static final int BOTTOM_VIEW_HEIGHT_LANDSCAPE = 32;
    public static final int BOTTOM_VIEW_HEIGHT_PORTRAIT = 49;
}
