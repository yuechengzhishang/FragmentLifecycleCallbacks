package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Application;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LoggerActivityLifecycleCallbacks());
    }

}
