package com.peitong.hotfixapp.hotfix;

import android.text.TextUtils;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by peitong.
 */
public class PathXmlParser {
    public final static String TAG_UPDATE_INFO = "data";
    public final static String TAG_MODULEVERSION_NUMBER = "ver";
    public final static String TAG_MODULE_URL = "pathUrl";
    public final static String TAG_MD5 = "md5";

    public final static String TAG_PATCH_VER = "patchver";

    public final static String TAG_ENABLE = "enable";

    public final static String TAG_EXPIRED = "expired";

    /**
     * 解析升级配置文件
     *
     * @param content 配置文件XML字符串
     * @return UpdateInfo 解析后的实体类
     * @throws UpdateException
     */
    public HotfixInfo parse(String content) throws UpdateException {


        if (content.trim().equals("")) {
            return null;
        }
        HotfixInfo info = null;

        if (TextUtils.isEmpty(content)) {
            throw new UpdateException(UpdateException.PARSE_ERROR);
        }

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(content));

            info = parseUpdateInfo(xpp);
        } catch (XmlPullParserException e) {
            throw new UpdateException(UpdateException.PARSE_ERROR);
        } catch (IOException e) {
            throw new UpdateException(UpdateException.PARSE_ERROR);
        }

        return info;
    }

    /**
     * XML转换为VO
     *
     * @param xpp
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private HotfixInfo parseUpdateInfo(XmlPullParser xpp) throws XmlPullParserException, IOException {
        HotfixInfo info = null;
        String currentTag = null;

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    currentTag = xpp.getName();
                    if (currentTag.equals(TAG_UPDATE_INFO)) {
                        info = new HotfixInfo();
                    } else if (currentTag.equals(TAG_MODULEVERSION_NUMBER)) {
                        if (info != null) {
                            info.setVersion(xpp.nextText());
                        }
                    } else if (currentTag.equals(TAG_MODULE_URL)) {
                        if (info != null) {
                            info.setUrl(xpp.nextText());
                        }
                    } else if (currentTag.equals(TAG_MD5)) {
                        info.setMd5(xpp.nextText());
                    }else if (currentTag.equals(TAG_PATCH_VER)) {
                        info.setPatchVer(xpp.nextText());
                    } else if (currentTag.equals(TAG_ENABLE)) {
                        int en = Integer.parseInt(xpp.nextText());
                        info.setEnalbe(en);
                    }/* else if (currentTag.equals(TAG_EXPIRED)) {
                        info.setExpired(xpp.nextText());
                    } */
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.TEXT:
                    break;
                default:
                    break;
            }
            eventType = xpp.next();
        }

        return info;
    }
}
