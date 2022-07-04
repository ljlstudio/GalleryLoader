package com.lee.album.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.ExifInterface
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:39
 * Package: com.lee.album.utils
 * desc   :
 */
class Utils {

    companion object {
        /**
         * 读取照片旋转角度
         *
         * @param path 照片路径
         * @return 角度
         */
        fun readPictureDegree(path: String?): Int {
            var degree = 0
            try {
                val exifInterface = ExifInterface(path)
                val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return degree
        }

        /**
         * 获取最近30天时间戳
         *
         * @return
         * @throws ParseException
         */
        @get:Throws(ParseException::class)
        val stateTime: Long
            get() {
                @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val c = Calendar.getInstance()
                c.add(Calendar.DATE, -30)
                val time = c.time
                return time.time
            }

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }

        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context?): Int {
            try {
                val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val outMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(outMetrics)
                return outMetrics.widthPixels
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun existsFile(file: File?): Boolean {
            return file != null && file.exists()
        }
    }


}