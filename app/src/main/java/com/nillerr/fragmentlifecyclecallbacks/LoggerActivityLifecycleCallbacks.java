package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class LoggerActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "LoggerCallbacks";

    private Map<Activity, FragmentLifecycleWatcher> mWatchers = new HashMap<Activity, FragmentLifecycleWatcher>();
    private final FragmentLifecycleCallbacks mFragmentCallbacks = new FragmentLifecycleCallbacks() {
        @Override
        public void onAdded(Fragment fragment) {
            Log.e(TAG, "onAdded: " + fragment);
        }

        @Override
        public void onRemoved(Fragment fragment) {
            Log.e(TAG, "onRemoved: " + fragment);
        }
    };

    private Map<Activity, SupportFragmentLifecycleWatcher> mSupportWatchers = new HashMap<Activity, SupportFragmentLifecycleWatcher>();
    private final SupportFragmentLifecycleCallbacks mSupportFragmentCallbacks = new SupportFragmentLifecycleCallbacks() {
        @Override
        public void onAdded(android.support.v4.app.Fragment fragment) {
            Log.e(TAG, "onAdded: " + fragment);
        }

        @Override
        public void onRemoved(android.support.v4.app.Fragment fragment) {
            Log.e(TAG, "onRemoved: " + fragment);
        }
    };

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG, "onActivityResumed: " + activity);

        if (activity instanceof ActionBarActivity) {
            onSupportActivityResumed((ActionBarActivity) activity);
        } else {
            FragmentLifecycleWatcher watcher = mWatchers.get(activity);
            if (watcher == null) {
                watcher = new FragmentLifecycleWatcher(activity, mFragmentCallbacks);
                mWatchers.put(activity, watcher);
            }

            watcher.start();
        }
    }

    public void onSupportActivityResumed(ActionBarActivity activity) {
        SupportFragmentLifecycleWatcher watcher = mSupportWatchers.get(activity);
        if (watcher == null) {
            watcher = new SupportFragmentLifecycleWatcher(activity, mSupportFragmentCallbacks);
            mSupportWatchers.put(activity, watcher);
        }

        watcher.start();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityPaused: " + activity);

        if (activity instanceof ActionBarActivity) {
            onSupportActivityPaused((ActionBarActivity) activity);
        } else {
            FragmentLifecycleWatcher watcher = mWatchers.get(activity);
            if (watcher != null) {
                if (activity.isFinishing()) {
                    watcher.stop();
                } else {
                    watcher.pause();
                }
            }
        }
    }

    private void onSupportActivityPaused(ActionBarActivity activity) {
        SupportFragmentLifecycleWatcher watcher = mSupportWatchers.get(activity);
        if (watcher != null) {
            if (activity.isFinishing()) {
                watcher.stop();
            } else {
                watcher.pause();
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
