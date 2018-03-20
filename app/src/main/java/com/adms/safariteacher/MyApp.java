package com.adms.safariteacher;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
