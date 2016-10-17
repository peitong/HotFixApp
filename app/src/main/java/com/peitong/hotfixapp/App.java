package com.peitong.hotfixapp;

import android.app.Application;

/**
 * 全局共享Application
 *
 * @author peitong
 */
public class App extends Application {

    private static App instance;

    /**
     * Creates a new instance of App.
     */
    public App() {
        super();
        instance = this;
    }

    /**
     * 获取全局唯一的app对象
     *
     * @return 全局唯一的app对象
     */
    public static App getApp() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


}
