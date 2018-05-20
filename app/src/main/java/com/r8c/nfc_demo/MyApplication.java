package com.r8c.nfc_demo;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class MyApplication  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "6af692975a2571f7fce414756e4fd74b");
    }
}
