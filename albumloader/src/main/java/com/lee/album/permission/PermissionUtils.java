package com.lee.album.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public final class PermissionUtils {

    private static final String FRAGMENT_DIALOG = "dialog";


    // 请求存储权限
    public static final int REQUEST_STORAGE_PERMISSION = 0x01;

    public static String[] CLEAN_STORAGE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private PermissionUtils() {
    }

    /**
     * 检查某个权限是否授权
     *
     * @param permission
     * @return
     */
    public static boolean permissionChecking(@NonNull Context context, @NonNull String permission) {
        int targetVersion = 1;
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            targetVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {

        }
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && targetVersion >= Build.VERSION_CODES.M) {
            result = (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED);
        } else {
            result = (PermissionChecker.checkSelfPermission(context, permission)
                    == PermissionChecker.PERMISSION_GRANTED);
        }
        return result;
    }

    /**
     * 判断是否可以读取本地媒体
     *
     * @return
     */
    public static boolean isStorageEnable(Context context) {
        return PermissionUtils.permissionChecking(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 检查某个权限是否授权
     *
     * @param fragment
     * @param permission
     * @return
     */
    public static boolean permissionChecking(@NonNull Fragment fragment, @NonNull String permission) {
        if (fragment.getContext() != null) {
            return permissionChecking(fragment.getContext(), permission);
        }
        return false;
    }

    /**
     * 检查权限列表是否授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean permissionsChecking(@NonNull Context context, @NonNull String[] permissions) {
        int targetVersion = 1;
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            targetVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {

        }

        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && targetVersion >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                result = (ContextCompat.checkSelfPermission(context, permissions[i])
                        == PackageManager.PERMISSION_GRANTED);
                if (!result) {
                    break;
                }
            }
        } else {
            for (int i = 0; i < permissions.length; i++) {
                result = (PermissionChecker.checkSelfPermission(context, permissions[i])
                        == PermissionChecker.PERMISSION_GRANTED);
                if (!result) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 检查权限列表是否授权
     *
     * @param fragment
     * @param permissions
     * @return
     */
    public static boolean permissionsChecking(@NonNull Fragment fragment, @NonNull String[] permissions) {
        if (fragment.getContext() == null) {
            return false;
        }
        return permissionsChecking(fragment.getContext(), permissions);
    }


    /**
     * 请求权限列表
     *
     * @param fragment
     * @param permissions
     * @param request_code
     */
    public static void requestPermissions(@NonNull Fragment fragment, @NonNull String[] permissions, int request_code) {
        boolean hasPermissions = permissionsChecking(fragment, permissions);
        if (!hasPermissions) {
            fragment.requestPermissions(permissions, request_code);
        }
    }

    /**
     * 请求权限列表
     *
     * @param activity
     * @param permissions
     */
    public static void requestPermissions(@NonNull FragmentActivity activity, @NonNull String[] permissions, int request_code) {
        boolean hasPermissions = permissionsChecking(activity, permissions);
        if (!hasPermissions) {
            ActivityCompat.requestPermissions(activity, permissions, request_code);
        }
    }

    /**
     * 打开权限设置页面
     *
     * @param fragment
     */
    public static void launchPermissionSettings(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return;
        }
        launchPermissionSettings(fragment.getActivity());
    }

    /**
     * 打开权限设置页面
     *
     * @param activity
     */
    public static void launchPermissionSettings(@NonNull Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }
}
