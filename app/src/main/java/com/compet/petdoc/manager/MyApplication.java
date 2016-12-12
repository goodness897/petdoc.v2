package com.compet.petdoc.manager;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tacademy on 2016-08-29.
 */
public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

}
