package com.peitong.hotfixapp.hotfix;

/**
 * Created by peitong.
 */
public class HotfixInfo {

    private String version = "";

    /**
     * 补丁对应的app版本号
     * @return
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    //path的下载地址
    private String url = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String md5 = "";

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    private String patchVer = "0";

    /**
     * 补丁版本号
     * @return
     */
    public String getPatchVer() {
        return patchVer;
    }

    public void setPatchVer(String patchnum) {
        this.patchVer = patchnum;
    }


    private int enalbe = 1;

    /**
     * 补丁包是否可用，1:可用，0:不可用
     * 是总开关
     * @return
     */
    public int getEnalbe() {
        return enalbe;
    }

    public void setEnalbe(int enalbe) {
        this.enalbe = enalbe;
    }

}
