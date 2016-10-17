package com.peitong.hotfixapp.hotfix;

import com.google.gson.Gson;
import com.peitong.hotfixapp.App;
import com.peitong.hotfixapp.hotfix.util.AndroidUtil;
import com.peitong.hotfixapp.hotfix.util.SharedPreferencesUtil;
import com.peitong.hotfixapp.hotfix.util.StringUtils;

/**
 * Created by peitong.
 */
public class HotFixInfoUtils {


    public static final String KEY_HOTFIX_INFO_SP = "key_hotfix_info_sp";
    private static HotfixInfo info;

    /**
     * 获取本地热修复的全部信息
     *
     * @return
     */
    public static HotfixInfo getHotfixInfo() {
        String spInfo = SharedPreferencesUtil.getInstance().getString(KEY_HOTFIX_INFO_SP);

        HotfixInfo resut = null;
        try {
            resut = (new Gson()).fromJson(spInfo, HotfixInfo.class);
        } catch (Exception e) {

        }

        return resut;
    }

    /**
     * 热修复信息是否与当前版本匹配
     *
     * @param info
     * @return
     */
    public static boolean isCurrentVersionInfo(HotfixInfo info) {
        boolean result = false;
        if (info == null)
            return result;

        String appV = AndroidUtil.getAppVersionName(App.getApp());
        if (!StringUtils.isNullOrEmpty(appV) && appV.equals(info.getVersion())) {
            result = true;
        }
        return result;
    }

    /**
     * 保存热修复信息到本地
     *
     * @param info
     */
    public static void saveInfo(HotfixInfo info) {
        if (info == null || !isCurrentVersionInfo(info)) {
            return;
        }

        try {
            String jsonStr = (new Gson()).toJson(info);
            if (!StringUtils.isNullOrEmpty(jsonStr)) {
                SharedPreferencesUtil.getInstance().setString(KEY_HOTFIX_INFO_SP, jsonStr);
            }
        } catch (Exception e) {

        }

    }


}
