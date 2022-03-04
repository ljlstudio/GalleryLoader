package com.lee.album.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:39
 * Package: com.lee.album.utils
 * desc   :
 */
public class Utils {
    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 获取最近30天时间戳
     *
     * @return
     * @throws ParseException
     */
    public static long getStateTime() throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, -30);

        Date time = c.getTime();


        return time.getTime();

    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static boolean existsFile(File file) {
        return file != null && file.exists();
    }
}