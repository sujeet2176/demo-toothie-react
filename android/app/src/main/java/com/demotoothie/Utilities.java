package com.demotoothie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utilities {

    // 主目录名
    private static final String HOME_PATH_NAME = "MediaStream";
    // 照片和视频的子目录名
    private static final String PHOTO_PATH_NAME = "Images";
    private static final String VIDEO_PATH_NAME = "Videos";
    private static final String CARD_MEDIA_PATH_NAME = "CardMedia";
    private static final String CARD_MEDIA_IMAGE_PATH_NAME = "Image";
    private static final String CARD_MEDIA_VIDEO_PATH_NAME = "Video";
    // 照片和视频的扩展名
    private static final String PHOTO_FILE_EXTENSION_1 = "png";
    private static final String PHOTO_FILE_EXTENSION_2 = "jpg";
    private static final String VIDEO_FILE_EXTENSION_1 = "avi";
    private static final String VIDEO_FILE_EXTENSION_2 = "mp4";

    /**
     * 获取应用数据主目录
     * @return  主目录路径
     */
    static public String getHomePath(Context context) {
        String homePath = null;

        try {
            String extStoragePath = Objects.requireNonNull(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getPath();
            File homeFile = new File(extStoragePath, HOME_PATH_NAME);
            homePath = homeFile.getCanonicalPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return homePath;
    }

    /**
     * 获取父目录下子目录
     */
    static public String getSubDir(String parent, String dir) {
        if (parent == null)
            return null;

        String subDirPath = null;

        try {
            // 获取展开的子目录路径
            File subDirFile = new File(parent, dir);

            if (!subDirFile.exists())
                subDirFile.mkdirs();

            subDirPath = subDirFile.getCanonicalPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return subDirPath;
    }

    /**
     * 获取主目录下照片目录
     * @return  照片目录路径
     */
    static public String getPhotoPath(Context context) {
        return getSubDir(getHomePath(context), PHOTO_PATH_NAME);
    }

    /**
     * 获取主目录下视频目录
     * @return  视频目录路径
     */
    static public String getVideoPath(Context context) {
        return getSubDir(getHomePath(context), VIDEO_PATH_NAME);
    }

    /**
     * 获取主目录下卡媒体目录
     * @return
     */
    static private String getCardMediaPath(Context context) {
        return getSubDir(getHomePath(context), CARD_MEDIA_PATH_NAME);
    }

    /**
     * 获取卡媒体目录中视频目录
     * @return
     */
    static public String getCardMediaVideoPath(Context context) {
        return getSubDir(getCardMediaPath(context), CARD_MEDIA_VIDEO_PATH_NAME);
    }

    /**
     * 获取卡媒体目录下图像目录
     * @return
     */
    static public String getCardMediaImagePath(Context context) {
        return getSubDir(getCardMediaPath(context), CARD_MEDIA_IMAGE_PATH_NAME);
    }

    /**
     * 载入照片文件路径列表
     * @return  照片文件路径列表
     */
    static public List<String> loadPhotoList(Context context) {
        String photoPathName = getPhotoPath(context);
        File photoPath = new File(photoPathName);
        List<String> photoFileNameList = new ArrayList<>();

        // 使用过滤器过滤照片文件列表
        File[] photoFiles = photoPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // 根据扩展名过滤照片文件
                try {
                    String filePath = file.getCanonicalPath();
                    String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
                    if (extension.equalsIgnoreCase(PHOTO_FILE_EXTENSION_1)
                            || extension.equalsIgnoreCase(PHOTO_FILE_EXTENSION_2)) {
                        return true;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        if (photoFiles != null) {
            // 排序，最新修改的在前
            List<File> photoFilesList = Arrays.asList(photoFiles);
            Collections.sort(photoFilesList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    long d = o1.lastModified() - o2.lastModified();
                    if (d > 0)
                        return -1;
                    else if (d < 0)
                        return 1;
                    else
                        return 0;
                }
            });
            photoFiles = photoFilesList.toArray(new File[0]);

            // 使用List装载照片文件路径
            for (File file : photoFiles) {
                // 逐个添加路径到列表
                photoFileNameList.add(file.getPath());
            }
        }

        return photoFileNameList;
    }

    /**
     * 载入视频文件路径列表
     * @return  视频文件路径列表
     */
    static public List<String> loadVideoList(Context context) {
        File videoPath = new File(getVideoPath(context));
        List<String> videoFileNameList = new ArrayList<>();

        // 使用过滤器过滤视频文件列表
        File[] videoFiles = videoPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // 根据扩展名过滤视频文件
                try {
                    String filePath = file.getCanonicalPath();
                    String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
                    if (extension.equalsIgnoreCase(VIDEO_FILE_EXTENSION_1)
                            || extension.equalsIgnoreCase(VIDEO_FILE_EXTENSION_2)) {
                        return true;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        if (videoFiles != null) {
            // 排序，最新修改的在前
            List<File> videoFilesList = Arrays.asList(videoFiles);
            Collections.sort(videoFilesList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    long d = o1.lastModified() - o2.lastModified();
                    if (d > 0)
                        return -1;
                    else if (d < 0)
                        return 1;
                    else
                        return 0;
                }
            });
            videoFiles = videoFilesList.toArray(new File[0]);

            // 使用List装载视频文件列表
            for (File file : videoFiles) {
                // 逐个添加路径到列表
                videoFileNameList.add(file.getPath());
            }
        }

        return videoFileNameList;
    }

    /**
     * 获取图片目录路径
     * @return  图片目录路径
     */
    static public String getPhotoDirPath(Context context) {
        String photoPath = getPhotoPath(context);
        if (photoPath == null)
            return null;

        // 如果文件夹不存在, 则创建
        File photoDir = new File(photoPath);
        if (!photoDir.exists()) {
            // 创建失败则返回null
            if (!photoDir.mkdirs()) return null;
        }

        return photoDir.getAbsolutePath();
    }

    /**
     * 获取视频目录路径
     * @return  视频目录路径
     */
    static public String getVideoDirPath(Context context) {
        String videoPath = getVideoPath(context);
        if (videoPath == null)
            return null;

        // 如果文件夹不存在, 则创建
        File videoDir = new File(videoPath);
        if (!videoDir.exists()) {
            // 创建失败则返回null
            if (!videoDir.mkdirs()) return null;
        }

        return videoDir.getAbsolutePath();
    }

    /**
     * 获取媒体文件名称
     * @return  媒体文件名称
     */
    static public String getMediaFileName() {
        // 由日期创建文件名
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault());
        //        String photoFileName = dateString + "." + PHOTO_FILE_EXTENSION;

        return format.format(date);
    }

    /**
     * Add border to bitmap
     * @param bmp           位图
     * @param color         颜色
     * @param borderSize    线宽
     * @return              Bitmap with border
     */
    static public Bitmap addBorderToBitmap(Bitmap bmp, int color, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(
                bmp.getWidth() + borderSize * 2,
                bmp.getHeight() + borderSize * 2,
                bmp.getConfig()
        );
        Canvas canvas = new Canvas(bmpWithBorder);
        // Set paint
        Paint paint = new Paint();
        paint.setColor(color);
        // Draw top
        canvas.drawLine(0, 0, bmp.getWidth() + borderSize * 2, borderSize * 2, paint);
        // Draw bottom
        canvas.drawLine(0, bmp.getHeight() + borderSize, bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, paint);
        // Draw left
        canvas.drawLine(0, 0, borderSize, bmp.getHeight() + borderSize * 2, paint);
        // Draw right
        canvas.drawLine(bmp.getWidth() + borderSize, 0, bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, paint);
        // Draw Original Image
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    /**
     * 删除文件
     * @param filePath  文件路径
     * @return  是否成功删除（路径为空或者文件不存在均返回true）
     */
    static public boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return true;
        }

        File file  = new File(filePath);

        return !file.exists() || file.delete();
    }

    /**
     * 获取存储目录（卡）的可用空间
     */
    static public long getFreeDiskSpace(Context context) {
        long size = 0;

        String homePath = getHomePath(context);
        if (homePath != null) {
            File homeDir = new File(homePath);
            size = homeDir.getFreeSpace();
        }

        return size;
    }

    /**
     * 格式化Size
     */
    public static String memoryFormatter(long size) {
        double bytes = 1.0 * size;
        double kilobytes = bytes / 1024;
        double megabytes = bytes / (1024 * 1024);
        double gigabytes = bytes / (1024 * 1024 * 1024);

        if (gigabytes >= 1.0)
            return String.format(Locale.getDefault(), "%1.2f GB", gigabytes);
        else if (megabytes >= 1.0)
            return String.format(Locale.getDefault(), "%1.2f MB", megabytes);
        else if (kilobytes >= 1.0)
            return String.format(Locale.getDefault(), "%1.2f KB", kilobytes);
        else
            return String.format(Locale.getDefault(), "%1.0f bytes", bytes);
    }

}
