package com.peitong.hotfixapp.hotfix;

/**
 * @author peitong <br/>
 * @description: 升级异常定义
 */
public class UpdateException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 未知异常
     */
    public static final int UNKNOWN = 0;

    /**
     * 升级配置信息非法
     */
    public static final int UPDATE_OPTIONS_NOT_VALID = 2;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 3;

    /**
     * 加载异常
     */
    public static final int LOAD_ERROR = 4;

    /**
     * 下载APK异常
     */
    public static final int DOWNLOAD_ERROR = 5;

    private int code = UNKNOWN;

    public UpdateException() {
        super(generateMessageFromCode(UNKNOWN));
        this.code = UNKNOWN;
    }

  /**
   * 生成异常提示信息
   * generateMessageFromCode:(这里用一句话描述这个方法的作用). <br/>
   * @param code
   * @return
   */
    private static String generateMessageFromCode(int code) {
        return "加载更新信息失败，错误码:"+code;
    }

    public UpdateException(int code) {
        super(generateMessageFromCode(code));
        this.code = code;
    }

    public UpdateException(String message) {
        super(message);
        this.code = UNKNOWN;
    }

    public UpdateException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
