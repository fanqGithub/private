package com.commai.commaplayer;

import android.app.Application;

import com.commai.commaplayer.shareprefrence.Preferences;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class PlayerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(this);
    }

}
