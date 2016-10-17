package com.peitong.hotfixapp.hotfix.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * Created by peitong.
 */
public class FileUtil {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static String mTag = "FileUtil";

    public FileUtil() {
    }

    public static String formatSize(long size) {
        float ONE_KB = 1024.0F;
        float ONE_MB = ONE_KB * ONE_KB;
        float ONE_GB = ONE_KB * ONE_MB;
        DecimalFormat df = new DecimalFormat("0.00");
        String displaySize;
        if((float)size >= ONE_KB && (float)size < ONE_MB) {
            displaySize = df.format((double)((float)size / ONE_KB)) + " KB";
        } else if((float)size >= ONE_MB && (float)size < ONE_GB) {
            displaySize = df.format((double)((float)size / ONE_MB)) + " MB";
        } else if((float)size >= ONE_GB) {
            displaySize = df.format((double)((float)size / ONE_GB)) + " GB";
        } else {
            displaySize = df.format(size) + " B";
        }

        return displaySize;
    }

    public static void deleteFileDir(File dir) {
        try {
            if(dir.exists() && dir.isDirectory()) {
                if(dir.listFiles().length == 0) {
                    dir.delete();
                } else {
                    File[] e = dir.listFiles();
                    int len = dir.listFiles().length;

                    for(int j = 0; j < len; ++j) {
                        if(e[j].isDirectory()) {
                            deleteFileDir(e[j]);
                        } else {
                            boolean isDeltet = e[j].delete();
                        }
                    }

                    e = null;
                }

                if(dir.listFiles().length == 0) {
                    dir.delete();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static boolean deleteFile(File file) {
        try {
            if(file != null && file.isFile() && file.exists()) {
                return file.delete();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return false;
    }

    public static boolean copyFile(File sourceFile, File destFile) {
        boolean isCopyOk = false;
        Object buffer = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String canonicalPath = "";

        try {
            canonicalPath = destFile.getCanonicalPath();
        } catch (IOException var17) {
            var17.printStackTrace();
        }

        if(!destFile.exists() && canonicalPath.lastIndexOf(File.separator) >= 0) {
            canonicalPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(File.separator));
            File e = new File(canonicalPath);
            if(!e.exists()) {
                e.mkdirs();
            }
        }

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFile), 8192);
            bos = new BufferedOutputStream(new FileOutputStream(destFile), 8192);
            byte[] buffer1 = new byte[8192];
            boolean e1 = false;

            int e2;
            while((e2 = bis.read(buffer1, 0, 8192)) != -1) {
                bos.write(buffer1, 0, e2);
            }

            bos.flush();
            isCopyOk = sourceFile != null && sourceFile.length() == destFile.length();
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            try {
                if(bos != null) {
                    bos.close();
                    bos = null;
                }

                if(bis != null) {
                    bis.close();
                    bis = null;
                }

                buffer = null;
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return isCopyOk;
    }

    public static byte[] readFileToBytes(File file) {
        byte[] bytes = null;
        if(file.exists()) {
            Object buffer = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            ByteArrayOutputStream baos = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(file), 8192);
                baos = new ByteArrayOutputStream();
                bos = new BufferedOutputStream(baos, 8192);
                byte[] buffer1 = new byte[8192];
                boolean e = false;

                int e1;
                while((e1 = bis.read(buffer1, 0, 8192)) != -1) {
                    bos.write(buffer1, 0, e1);
                }

                bos.flush();
                bytes = baos.toByteArray();
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } catch (IOException var18) {
                var18.printStackTrace();
            } finally {
                try {
                    if(bos != null) {
                        bos.close();
                        bos = null;
                    }

                    if(baos != null) {
                        baos.close();
                        baos = null;
                    }

                    if(bis != null) {
                        bis.close();
                        bis = null;
                    }

                    buffer = null;
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

            }
        }

        return bytes;
    }

    public static byte[] readFileToBytes(File file, long offset, long len) {
        byte[] bytes = null;
        if(file.exists() && offset >= 0L && len > offset && offset < file.length()) {
            RandomAccessFile raf = null;
            ByteArrayOutputStream bos = null;

            try {
                raf = new RandomAccessFile(file, "r");
                raf.seek(offset);
                bos = new ByteArrayOutputStream();
                boolean e = true;

                int var21;
                for(long count = offset; (var21 = raf.read()) != -1 && count < len; ++count) {
                    bos.write(var21);
                }

                bos.flush();
                bytes = bos.toByteArray();
            } catch (IOException var19) {
                var19.printStackTrace();
            } finally {
                try {
                    if(raf != null) {
                        raf.close();
                        raf = null;
                    }

                    if(bos != null) {
                        bos.close();
                        bos = null;
                    }
                } catch (IOException var18) {
                    var18.printStackTrace();
                }

            }
        }

        return bytes;
    }

    public static boolean writeBytesToFile(File file, byte[] bytes, long offset) {
        boolean isOk = false;
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var18) {
                var18.printStackTrace();
            }
        }

        if(file.exists() && bytes != null && offset >= 0L) {
            RandomAccessFile raf = null;

            try {
                raf = new RandomAccessFile(file, "rw");
                raf.seek(offset);
                raf.write(bytes);
                isOk = true;
            } catch (IOException var16) {
                var16.printStackTrace();
            } finally {
                try {
                    if(raf != null) {
                        raf.close();
                        raf = null;
                    }
                } catch (IOException var15) {
                    var15.printStackTrace();
                }

            }
        }

        return isOk;
    }

    public static String readFileToString(File file) {
        return readFileToString(file, (String)null);
    }

    public static String readFileToString(File file, String encoding) {
        String result = null;
        if(file.exists()) {
            Object buffer = null;
            BufferedReader br = null;
            InputStreamReader isr = null;
            BufferedWriter bw = null;
            StringWriter sw = new StringWriter();

            try {
                isr = encoding == null?new InputStreamReader(new FileInputStream(file)):new InputStreamReader(new FileInputStream(file), encoding);
                br = new BufferedReader(isr);
                bw = new BufferedWriter(sw);
                char[] buffer1 = new char[8192];
                boolean e = false;

                int e1;
                while((e1 = br.read(buffer1, 0, 8192)) != -1) {
                    bw.write(buffer1, 0, e1);
                }

                bw.flush();
                result = sw.toString();
            } catch (FileNotFoundException var19) {
                var19.printStackTrace();
            } catch (IOException var20) {
                var20.printStackTrace();
            } finally {
                try {
                    if(bw != null) {
                        bw.close();
                        bw = null;
                    }

                    if(br != null) {
                        br.close();
                        br = null;
                    }

                    if(isr != null) {
                        isr.close();
                        isr = null;
                    }

                    if(sw != null) {
                        sw.close();
                        sw = null;
                    }

                    buffer = null;
                } catch (IOException var18) {
                    var18.printStackTrace();
                }

            }
        }

        return result;
    }

    public static boolean writeStringToFile(File file, String content) {
        return writeStringToFile(file, content, false);
    }

    public static boolean writeStringToFile(File file, String content, boolean isAppend) {
        boolean isWriteOk = false;
        Object buffer = null;
        int count = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            if(!file.exists()) {
                createNewFileAndParentDir(file);
            }

            if(file.exists()) {
                br = new BufferedReader(new StringReader(content));
                bw = new BufferedWriter(new FileWriter(file, isAppend));
                char[] buffer1 = new char[8192];

                int e1;
                for(boolean e = false; (e1 = br.read(buffer1, 0, 8192)) != -1; count += e1) {
                    bw.write(buffer1, 0, e1);
                }

                bw.flush();
            }

            isWriteOk = content.length() == count;
        } catch (IOException var17) {
            var17.printStackTrace();
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                    bw = null;
                }

                if(br != null) {
                    br.close();
                    br = null;
                }

                buffer = null;
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return isWriteOk;
    }

    public static boolean writeBytesToFile(File file, byte[] bytes) {
        return writeBytesToFile(file, bytes, false);
    }

    public static boolean writeBytesToFile(File file, byte[] bytes, boolean isAppend) {
        boolean isWriteOk = false;
        Object buffer = null;
        int count = 0;
        ByteArrayInputStream bais = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            if(!file.exists()) {
                createNewFileAndParentDir(file);
            }

            if(file.exists()) {
                bos = new BufferedOutputStream(new FileOutputStream(file, isAppend), 8192);
                bais = new ByteArrayInputStream(bytes);
                bis = new BufferedInputStream(bais, 8192);
                byte[] buffer1 = new byte[8192];

                int e1;
                for(boolean e = false; (e1 = bis.read(buffer1, 0, 8192)) != -1; count += e1) {
                    bos.write(buffer1, 0, e1);
                }

                bos.flush();
            }

            isWriteOk = bytes.length == count;
        } catch (FileNotFoundException var20) {
            var20.printStackTrace();
        } catch (IOException var21) {
            var21.printStackTrace();
        } finally {
            try {
                if(bos != null) {
                    bos.close();
                    bos = null;
                }

                if(bis != null) {
                    bis.close();
                    bis = null;
                }

                if(bais != null) {
                    bais.close();
                    bais = null;
                }

                buffer = null;
            } catch (IOException var19) {
                var19.printStackTrace();
            }

        }

        return isWriteOk;
    }

    public static boolean createParentDir(File file) {
        boolean isMkdirs = true;
        if(!file.exists()) {
            File dir = file.getParentFile();
            if(!dir.exists()) {
                isMkdirs = dir.mkdirs();
            }
        }

        return isMkdirs;
    }

    public static boolean createNewFileAndParentDir(File file) {
        boolean isCreateNewFileOk = true;
        isCreateNewFileOk = createParentDir(file);
        if(isCreateNewFileOk && !file.exists()) {
            try {
                isCreateNewFileOk = file.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
                isCreateNewFileOk = false;
            }
        }

        return isCreateNewFileOk;
    }

    public static String getFileExtensionFromUrl(String filename) {
        if(!TextUtils.isEmpty(filename)) {
            int dotPos = filename.lastIndexOf(46);
            if(0 <= dotPos) {
                return filename.substring(dotPos + 1);
            }
        }

        return "";
    }

    public static String getMD5(byte[] byteArray) {
        MessageDigest mMessageDigest = null;

        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        if(mMessageDigest != null) {
            mMessageDigest.update(byteArray, 0, byteArray.length);
            return bytesToHexString(mMessageDigest.digest());
        } else {
            return null;
        }
    }

    public static String getMD5(File file) {
        MessageDigest mMessageDigest = null;

        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        if(mMessageDigest != null) {
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(file);
                byte[] e = new byte[8192];

                int length;
                while((length = fis.read(e)) != -1) {
                    mMessageDigest.update(e, 0, length);
                }

                String var5 = bytesToHexString(mMessageDigest.digest());
                return var5;
            } catch (Exception var17) {
                return null;
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var15) {
                        ;
                    }
                }

            }
        } else {
            return null;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        if(bytes != null) {
            StringBuffer stringBuffer = new StringBuffer();

            for(int i = 0; i < bytes.length; ++i) {
                int v = bytes[i] & 255;
                String hv = Integer.toHexString(v);
                if(hv.length() < 2) {
                    stringBuffer.append(0);
                }

                stringBuffer.append(hv);
            }

            return stringBuffer.toString();
        } else {
            return null;
        }
    }

    public static short getShort(byte[] bytes, int index) {
        return (short)(bytes[index + 1] << 8 | bytes[index + 0] & 255);
    }

    public static byte getWaveLevel(short[] in, int pcmFrameSize) {
        boolean minlevel = false;
        boolean maxlevel = false;
        short var7 = 0;
        short var8 = 0;

        for(int j = 0; j < pcmFrameSize; ++j) {
            short w = in[j];
            if(w > var8) {
                var8 = w;
            }

            if(w < var7) {
                var7 = w;
            }
        }

        byte waveLevel;
        if(Math.abs(var8) > Math.abs(var7)) {
            waveLevel = (byte)(var8 >> 8);
        } else {
            waveLevel = (byte)(var7 >> 8);
        }

        return waveLevel;
    }
}
