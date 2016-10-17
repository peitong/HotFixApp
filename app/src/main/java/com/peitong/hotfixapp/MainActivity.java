package com.peitong.hotfixapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.peitong.hotfixapp.hotfix.HotfixProxy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkHotfixPatchUpdateStatus();
        setContentView(R.layout.activity_main);
    }

    private void checkHotfixPatchUpdateStatus() {
        HotfixProxy hotfixProxy = new HotfixProxy(this);
        hotfixProxy.checkPatchUpateStatus();
    }
}
