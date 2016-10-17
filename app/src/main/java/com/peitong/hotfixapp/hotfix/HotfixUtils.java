package com.peitong.hotfixapp.hotfix;

/**
 * Created by peitong.
 */
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.peitong.hotfixapp.hotfix.util.AssetUtils;
import com.peitong.hotfixapp.hotfix.util.DexUtils;
import com.peitong.hotfixapp.hotfix.util.SecurityChecker;
import java.io.File;
import java.io.IOException;

public class HotfixUtils {
    private static final String TAG = "hotfix";
    private static final String HACK_DEX = "hack.jar";
    private static final String DEX_DIR = "hotfix";
    private static final String DEX_OPT_DIR = "hotfixopt";
    private static SecurityChecker mSecurityChecker;
    private static File mDexDir;

    public HotfixUtils() {
    }

    public static void init(Context context) throws HotfixException {
        mSecurityChecker = new SecurityChecker(context);
        mDexDir = new File(context.getFilesDir(), "hotfix");
        mDexDir.mkdir();
        Log.d("hotfix", mDexDir.getAbsolutePath());
        String dexPath = null;

        try {
            dexPath = AssetUtils.copyAsset(context, "hack.jar", mDexDir);
        } catch (IOException var3) {
            Log.e("hotfix", "copy hack.jar failed");
            throw new HotfixException(var3.getMessage());
        }

        loadPatch(context, dexPath);
    }

    public static void loadPatch(Context context, String dexPath) throws HotfixException {
        if(context != null && !TextUtils.isEmpty(dexPath)) {
            File dexFile = new File(dexPath);
            if(!dexFile.exists()) {
                Log.e("hotfix", dexPath + " is null");
                throw new HotfixException(dexPath + " is null");
            } else if(!mSecurityChecker.verifyApk(dexFile)) {
                Log.e("hotfix", dexPath + "verifyApk failed");
                throw new HotfixException("verifyApk failed");
            } else {
                File dexOptDir = new File(context.getFilesDir(), "hotfixopt");
                dexOptDir.mkdir();

                try {
                    DexUtils.injectDexAtFirst(dexPath, dexOptDir.getAbsolutePath());
                    Log.d("hotfix", "loadPatch success:" + dexPath);
                } catch (Exception var5) {
                    Log.e("hotfix", "inject " + dexPath + " failed");
                    var5.printStackTrace();
                    throw new HotfixException(var5.getMessage());
                }
            }
        } else {
            Log.e("hotfix", "context is null");
            throw new HotfixException("context is null");
        }
    }
}
