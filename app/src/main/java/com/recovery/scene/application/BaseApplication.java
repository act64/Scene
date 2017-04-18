package com.recovery.scene.application;

import android.app.Application;
import android.content.Context;

import com.recovery.scene.utils.ImageLoadManager;

/**
 * Created by tom on 2017/4/18.
 */

public class BaseApplication extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        ImageLoadManager.init();

    }
}
