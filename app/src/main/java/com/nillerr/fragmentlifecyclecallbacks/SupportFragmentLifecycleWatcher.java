package com.nillerr.fragmentlifecyclecallbacks;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

public class SupportFragmentLifecycleWatcher implements Runnable {

    private final Object mActivitySyncRoot = new Object();

    private ActionBarActivity mActivity;
    private boolean mRunning = false;

    private SupportFragmentLifecycleCallbacks mCallbacks;

    private List<Fragment> mFragmentsInLastPass = new ArrayList<Fragment>();

    public SupportFragmentLifecycleWatcher(ActionBarActivity activity, SupportFragmentLifecycleCallbacks callbacks) {
        mActivity = activity;
        mCallbacks = callbacks;
    }

    public void start() {
        if (!mRunning) {
            mRunning = true;

            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void pause() {
        mRunning = false;
    }

    public void stop() {
        mRunning = false;

        synchronized (mActivitySyncRoot) {
            // Release reference to Activity to avoid memory leaks
            mActivity = null;
            mCallbacks = null;
        }
    }

    private List<Fragment> findFragments(ActionBarActivity activity) {
        return new ArrayList<Fragment>(activity.getSupportFragmentManager().getFragments());
    }

    @Override
    public void run() {
        while (mRunning) {
            synchronized (mActivitySyncRoot) {
                List<Fragment> fragments = findFragments(mActivity);
                for (Fragment fragment : fragments) {
                    if (!mFragmentsInLastPass.contains(fragment)) {
                        mCallbacks.onAdded(fragment);
                    }
                }

                for (Fragment fragment : mFragmentsInLastPass) {
                    if (!fragments.contains(fragment)) {
                        mCallbacks.onRemoved(fragment);
                    }
                }

                mFragmentsInLastPass = fragments;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
