package com.yesway.design.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by gxl on 2017/12/5.
 */

public class BaseApplication extends MultiDexApplication {
    public static BaseApplication getApplication(){
        return app;
    }
    public static BaseApplication app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fresco.initialize(this);
//        Recovery.getInstance()
//                .debug(true)
//                .recoverInBackground(false)
//                .recoverStack(true)
//                .mainPage(BannerMainActivity.class)
//                .init(this);

    }
}
