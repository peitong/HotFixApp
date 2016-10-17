package com.peitong.hotfixapp.hotfix.util;

/**
 * Created by peitong.
 */
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.security.auth.x500.X500Principal;

public class SecurityChecker {
    private static final String TAG = "SecurityChecker";
    private static final String CLASSES_DEX = "classes.dex";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    private PublicKey mPublicKey;
    private boolean mDebuggable;

    public SecurityChecker(Context context) {
        this.init(context);
    }

    public boolean verifyApk(File path) {
        if(this.mDebuggable) {
            return true;
        } else {
            JarFile jarFile = null;

            boolean e1;
            try {
                boolean certs;
                try {
                    jarFile = new JarFile(path);
                    JarEntry e = jarFile.getJarEntry("classes.dex");
                    if(null == e) {
                        certs = false;
                        return certs;
                    }

                    this.loadDigestes(jarFile, e);
                    Certificate[] certs1 = e.getCertificates();
                    if(certs1 == null) {
                        e1 = false;
                        return e1;
                    }

                    e1 = this.check(path, certs1);
                } catch (IOException var17) {
                    Log.e("SecurityChecker", path.getAbsolutePath(), var17);
                    certs = false;
                    return certs;
                }
            } finally {
                try {
                    if(jarFile != null) {
                        jarFile.close();
                    }
                } catch (IOException var16) {
                    Log.e("SecurityChecker", path.getAbsolutePath(), var16);
                }

            }

            return e1;
        }
    }

    private void loadDigestes(JarFile jarFile, JarEntry je) throws IOException {
        InputStream is = null;

        try {
            is = jarFile.getInputStream(je);
            byte[] bytes = new byte[8192];

            while(true) {
                if(is.read(bytes) > 0) {
                    continue;
                }
            }
        } finally {
            if(is != null) {
                is.close();
            }

        }

    }

    private boolean check(File path, Certificate[] certs) {
        if(certs.length > 0) {
            int i = certs.length - 1;

            while(i >= 0) {
                try {
                    certs[i].verify(this.mPublicKey);
                    return true;
                } catch (Exception var5) {
                    Log.e("SecurityChecker", path.getAbsolutePath(), var5);
                    --i;
                }
            }
        }

        return false;
    }

    private void init(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = e.getPackageInfo(packageName, 64);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream stream = new ByteArrayInputStream(packageInfo.signatures[0].toByteArray());
            X509Certificate cert = (X509Certificate)certFactory.generateCertificate(stream);
            this.mDebuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
            this.mPublicKey = cert.getPublicKey();
        } catch (NameNotFoundException var8) {
            Log.e("SecurityChecker", "init", var8);
        } catch (CertificateException var9) {
            Log.e("SecurityChecker", "init", var9);
        }

    }
}
