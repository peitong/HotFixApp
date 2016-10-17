package com.peitong.hotfixapp.hotfix.util;

import android.util.Log;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * 跟andoird系统相关的工具类
 *
 * Created by peitong.
 */
public class AndroidUtil {


    /**
     * 返回当前程序版本名 
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---  
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.d("VersionInfo", "Exception");
        }
        return versionName;
    }



}
