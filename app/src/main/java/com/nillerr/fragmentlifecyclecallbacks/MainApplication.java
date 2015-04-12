package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Application;

/**
 * Created by nillerr on 12/04/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LoggerActivityLifecycleCallbacks());
    }

}
