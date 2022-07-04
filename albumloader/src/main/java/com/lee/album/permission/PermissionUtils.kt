package com.lee.album.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.FragmentActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

 class PermissionUtils {


    companion object {
        @JvmField
        var CLEAN_STORAGE_PERMISSIONS: Array<String> =  arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        const val FRAGMENT_DIALOG = "dialog"

        // 请求存储权限
        const val REQUEST_STORAGE_PERMISSION = 0x01


        /**
         * 检查权限列表是否授权
         *
         * @param context
         * @param permissions
         * @return
         */
          fun permissionsChecking(context: Context?, permissions: Array<String>): Boolean {
            var targetVersion = 1
            try {
                val info = context?.packageManager?.getPackageInfo(context.packageName, 0)
                targetVersion = info?.applicationInfo?.targetSdkVersion ?: 26
            } catch (e: PackageManager.NameNotFoundException) {
            }
            var result = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && targetVersion >= Build.VERSION_CODES.M
            ) {
                for (i in permissions.indices) {
                    result =
                        (context?.let { ContextCompat.checkSelfPermission(it, permissions[i]!!) }
                                == PackageManager.PERMISSION_GRANTED)
                    if (!result) {
                        break
                    }
                }
            } else {
                for (i in permissions.indices) {
                    result =
                        (context?.let {
                            PermissionChecker.checkSelfPermission(
                                it,
                                permissions[i]!!
                            )
                        }
                                == PermissionChecker.PERMISSION_GRANTED)
                    if (!result) {
                        break
                    }
                }
            }
            return result
        }


        /**
         * 检查某个权限是否授权
         *
         * @param permission
         * @return
         */
        private fun permissionChecking(context: Context, permission: String): Boolean {
            var targetVersion = 1
            try {
                val info = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                targetVersion = info.applicationInfo.targetSdkVersion
            } catch (e: PackageManager.NameNotFoundException) {
            }
            var result = false
            result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && targetVersion >= Build.VERSION_CODES.M
            ) {
                (ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED)
            } else {
                (PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED)
            }
            return result
        }

        /**
         * 判断是否可以读取本地媒体
         *
         * @return
         */
        fun isStorageEnable(context: Context): Boolean {
            return permissionChecking(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        /**
         * 检查某个权限是否授权
         *
         * @param fragment
         * @param permission
         * @return
         */
        fun permissionChecking(fragment: Fragment, permission: String): Boolean {
            return if (fragment.context != null) {
                permissionChecking(fragment.context!!, permission)
            } else false
        }


        /**
         * 检查权限列表是否授权
         *
         * @param fragment
         * @param permissions
         * @return
         */
        private fun permissionsChecking(fragment: Fragment, permissions: Array<String>): Boolean {
            return if (fragment.context == null) {
                false
            } else permissionsChecking(
                fragment.context!!,
                permissions
            )
        }

        /**
         * 请求权限列表
         *
         * @param fragment
         * @param permissions
         * @param request_code
         */
        fun requestPermissions(fragment: Fragment, permissions: Array<String>, request_code: Int) {
            val hasPermissions = permissionsChecking(fragment, permissions)
            if (!hasPermissions) {
                fragment.requestPermissions(permissions, request_code)
            }
        }

        /**
         * 请求权限列表
         *
         * @param activity
         * @param permissions
         */
        fun requestPermissions(
            activity: FragmentActivity,
            permissions: Array<String>,
            request_code: Int
        ) {
            val hasPermissions = permissionsChecking(activity, permissions)
            if (!hasPermissions) {
                ActivityCompat.requestPermissions(activity, permissions, request_code)
            }
        }

        /**
         * 打开权限设置页面
         *
         * @param fragment
         */
        fun launchPermissionSettings(fragment: Fragment) {
            if (fragment.activity == null) {
                return
            }
            launchPermissionSettings(fragment.activity!!)
        }

        /**
         * 打开权限设置页面
         *
         * @param activity
         */
        private fun launchPermissionSettings(activity: Activity) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", activity.packageName, null)
            activity.startActivity(intent)
        }
    }



}