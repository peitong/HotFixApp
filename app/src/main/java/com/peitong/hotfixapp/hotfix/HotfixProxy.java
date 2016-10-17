package com.peitong.hotfixapp.hotfix;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.peitong.hotfixapp.hotfix.util.Config;
import com.peitong.hotfixapp.hotfix.util.AndroidUtil;
import com.peitong.hotfixapp.hotfix.util.FileUtil;
import com.peitong.hotfixapp.hotfix.util.InputStreamUtils;
import com.peitong.hotfixapp.hotfix.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by peitong.
 */
public class HotfixProxy {
    private static final String TAG = "hotfix_proxy";
    private Context mContext;
    private Handler mHandler;


    public HotfixProxy(Context context) {
        mContext = context;
    }

    public void checkPatchUpateStatus() {
        new Thread(mCheckUpdateRunnable).start();
        HotfixInfo info = getLocalPatchStatus();
        try {
            String patchPath = getPatchFileByInfo(info);
            if (!StringUtils.isNullOrEmpty(patchPath)) {
                Log.d(TAG, "补丁文件目录：" + patchPath);
                File file = new File(patchPath);
                if (file.exists()) {
                    String patchMd5 = FileUtil.getMD5(file);
                    Log.d(TAG, "补丁信息以及补丁文件存在，md5值为：" + patchMd5);
                    if (patchMd5.equals(info.getMd5())) {
                        HotfixUtils.loadPatch(mContext, patchPath);
                        Log.d(TAG, "加载补丁包，补丁信息" + (new Gson().toJson(info)));
                    } else {
                        Log.d(TAG, "补丁文件存在，md5对不上");
                    }
                } else {
                    Log.d(TAG, "补丁文件不存在");
                }
            }
        } catch (Exception e) {

        }
    }

    public String getLocalHookVersion() {
        if (mContext != null)
            return AndroidUtil.getAppVersionName(mContext);
        else
            return "";
    }

    /**
     * 下载apk线程
     */
    class DownModuleRunnable implements Runnable {

        private HotfixInfo mHotfixInfo;

        DownModuleRunnable(HotfixInfo info) {
            mHotfixInfo = info;
        }

        @Override
        public void run() {
            Log.d(TAG, "执行下载逻辑");
            //参数校验
            if (mHotfixInfo == null || StringUtils.isNullOrEmpty(mHotfixInfo.getPatchVer()))
                return;

            String moduleurl = mHotfixInfo.getUrl();
            if (!URLUtil.isNetworkUrl(moduleurl)) {
                Log.e(TAG, "更新配置地址非法");
                return;
            }

            if (StringUtils.isNullOrEmpty(mHotfixInfo.getMd5())) {
                return;
            }

            Log.d(TAG, "开始下载安装包");

            FileOutputStream fos = null;
            InputStream is = null;

            try {
                //清除安装包目录
                String downloaddir = mContext.getFilesDir() + Config.HOTFIX_PATH_DOWNLOAD_DIR;
                File dir = new File(downloaddir);
                dir.delete();
                //创建下载目录
                dir.mkdir();

                Log.d(TAG, "下载路径：" + downloaddir);

                //清除原有模块目录
                String pathParentDirStr = getAllPatchDir();
                File parentdir = new File(pathParentDirStr);

                Log.d(TAG, "原有模块父路径：" + pathParentDirStr);
                if (parentdir.exists()) {

                    File[] files = parentdir.listFiles();
                    String tempPathDirStr = getCurrentVerFolderDir();
                    Log.d(TAG, "当前版本补丁总文件路径：" + tempPathDirStr);

                    for (File subFile : files) {
                        Log.d(TAG, "原有模块父路径下的文件路径：" + subFile.getPath());
                        if (StringUtils.isNullOrEmpty(tempPathDirStr)) {
                            FileUtil.deleteFileDir(subFile);
                        } else {
                            if (!tempPathDirStr.equals(subFile.getPath())) {
                                FileUtil.deleteFileDir(subFile);
                            }
                        }
                    }
                }


                String pathDirStr = getPatchFolderPathByInfo(mHotfixInfo);
                File path = new File(pathDirStr);
                //创建模块目录
                if (!path.exists()) {
                    path.mkdir();
                }

                Log.d(TAG, "创建b补丁父路径目录：" + downloaddir);

                //创建下载文件
                String newPatchFileName = mContext.getFilesDir() + Config.HOTFIX_PATH_DOWNLOAD_DIR + "/" + Config.HOTFIX_PATCH_FILE_NAME;
                File modulefile = new File(newPatchFileName);
                //下载文件逻辑
                fos = new FileOutputStream(modulefile);
                URL url = new URL(mHotfixInfo.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                is = conn.getInputStream();
                int readedLen = 0;//每次读取的字节数
                byte buf[] = new byte[1024];
                Looper.prepare();
                while ((readedLen = is.read(buf)) > 0) {
                    fos.write(buf, 0, readedLen);
                }

                fos.close();
                is.close();

                String patchMd5 = FileUtil.getMD5(modulefile);
                if (patchMd5.equals(mHotfixInfo.getMd5())) {
                    String patchPath =getPatchFileByInfo(mHotfixInfo);
                    FileUtil.copyFile(modulefile, new File(patchPath));
//                    HotfixUtils.loadPatch(mContext, patchPath);
                    Log.d(TAG, "download module file finished");
                } else {
                    modulefile.delete();
                    Log.d(TAG, "下载完毕，md5对不上，删除下载文件；文件md5是" + patchMd5);
                }


            } catch (Exception e) {
                Log.e(TAG, "download module file fail", e);
            } finally {
                fos = null;
                is = null;
            }
        }
    }

    /**
     * 检查更新线程
     */
    private Runnable mCheckUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("hotfix_proxy", "mCheckUpdateRunnable run");

            //下载XML
            String xml = null;

            try {
                String version = getLocalHookVersion();
                String hotfixUrl;
                if (!StringUtils.isNullOrEmpty(version)) {
                    hotfixUrl = Config.HOTFIX_CHECK_URL + "?jarver=" + version + "&t=" + System.currentTimeMillis();
                    Log.d(TAG, "配置文件url：" + hotfixUrl);
                } else {
                    return;
                }

                URL url = new URL(hotfixUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    xml = InputStreamUtils.InputStreamTOString(is);
                } else {
                    Log.d(TAG, "请求结果：" + conn.getResponseCode());
                    return;
                }

                Log.d(TAG, "升级文件信息====\n" + xml);
                //解析XML
                PathXmlParser xmlParser = new PathXmlParser();
                HotfixInfo tempInfo = xmlParser.parse(xml);
                if (tempInfo == null) {
                    Log.d(TAG, "解析错误");
                    return;
                }

                if(StringUtils.isNullOrEmpty(tempInfo.getPatchVer())) {
                    Log.d(TAG, "补丁包版本为空");
                    return;
                }

                if (!HotFixInfoUtils.isCurrentVersionInfo(tempInfo)) {
                    Log.d(TAG, "当前信息版本不对==========");
                    return;
                }

                Log.d(TAG, "保存热修复信息:" + (new Gson()).toJson(tempInfo));

                HotFixInfoUtils.saveInfo(tempInfo);
                if (tempInfo.getEnalbe() == 0) {
                    Log.d(TAG, "补丁文件不允许使用==========");
                    return;
                }

                //获取有效的补丁信息，判断是否需要下载
                HotfixInfo info = getLocalPatchStatus();

                if(info == null){
                    Log.d(TAG, "没有有效的补丁信息，不执行下载");
                    return;
                }

                String tempPath = getPatchFileByInfo(info);
                if(StringUtils.isNullOrEmpty(tempPath)){
                    return;
                }

                File patchFile = new File(tempPath);
                if (!StringUtils.isNullOrEmpty(info.getMd5()) && !patchFile.exists()) {
                    new DownModuleRunnable(info).run();
                } else {
                    if (patchFile.exists()) {
                        String patchMd5 = FileUtil.getMD5(patchFile);
                        if(!StringUtils.isNullOrEmpty(info.getMd5()) && info.getMd5().equals(patchMd5)) {
                            Log.d(TAG, "不执行下载，补丁文件已经存在");
                        } else {
                            Log.d(TAG, "执行下载，md5值不匹配");
                            patchFile.delete();
                            new DownModuleRunnable(info).run();
                        }

                    } else {
                        Log.d(TAG, "不执行下载，热修复文件md5值为空");
                    }
                }
            } catch (UpdateException e) {
            } catch (Exception e) {
            }
        }
    };

    /**
     * 获取补丁信息
     * enable为1，并且不再Expired中的数据
     *
     * @return
     */
    private HotfixInfo getLocalPatchStatus() {
        Log.d(TAG, "开始检测本地补丁信息");
        HotfixInfo lastInfo = HotFixInfoUtils.getHotfixInfo();

        if (lastInfo != null && lastInfo.getEnalbe() != 0) {
            Log.d(TAG, "本地补丁信息\n" + (new Gson().toJson(lastInfo)));
            return lastInfo;
        }

        Log.d(TAG, "本地没有获取到补丁信息或补丁enable为0");
        return null;
    }

    public String getAllPatchDir() {
        return mContext.getFilesDir() + Config.HOTFIX_PACTH_DIR;
    }

    /**
     * 获取当前app版本补丁文件夹
     *
     * @return
     */
    public String getCurrentVerFolderDir() {
        String appV = getLocalHookVersion();
        if (StringUtils.isNullOrEmpty(appV)) {
            return "";
        }
        return getAllPatchDir() + "/" + appV;
    }

    /**
     * 获取补丁文路径
     *
     * @return
     */
    public String getPatchFileByInfo(HotfixInfo info) {
        if (StringUtils.isNullOrEmpty(getPatchFolderPathByInfo(info))) {
            return "";
        }

        return getPatchFolderPathByInfo(info) + "/" + Config.HOTFIX_PATCH_FILE_NAME;
    }

    /**
     * 获取补丁文件夹路径
     *
     * @param info
     * @return
     */
    public String getPatchFolderPathByInfo(HotfixInfo info) {
        if (info == null || StringUtils.isNullOrEmpty(info.getPatchVer())) {
            return "";
        }
        String parentDir = getCurrentVerFolderDir();

        if (StringUtils.isNullOrEmpty(parentDir)) {
            return "";
        }

        return parentDir + "/" + info.getPatchVer();
    }


}
