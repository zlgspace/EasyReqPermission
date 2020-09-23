package com.zlgspace.easyreqpermission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static com.zlgspace.easyreqpermission.Constant.*;


public final class PermissionUtils {
    private PermissionUtils(){}

    /**
     * 请求权限
     * @param activity
     * @param permissions
     */
    public static void reqPermission(Activity activity, String ...permissions){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return;
        ActivityCompat.requestPermissions(activity,
                permissions,
                PERMISSIONS_REQUEST);
    }

    /**
     * 请求权限
     * @param fragment
     * @param permissions
     */
    public static void reqPermission(Fragment fragment, String ...permissions){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return;
        fragment.requestPermissions(
                permissions,
                PERMISSIONS_REQUEST);
    }

    /**
     * 请求权限
     * @param fragment
     * @param permissions
     */
    public static void reqPermission(android.app.Fragment fragment, String ...permissions){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return;
        fragment.requestPermissions(
                permissions,
                PERMISSIONS_REQUEST);
    }


    /**
     * 是否存在权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermssion(Context context, String permission){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return true;
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }

    /**
     * 检查是否可以请求权限，当用户拒绝并选择了不再提示后，返回ture
     * @param context
     * @param permission
     * @return
     */
    public static boolean canRequestPermission(Activity context, String permission){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return true;
        return !ActivityCompat.shouldShowRequestPermissionRationale(context,permission);
    }
}
