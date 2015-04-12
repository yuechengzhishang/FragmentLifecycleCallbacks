package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentLifecycleWatcher implements Runnable {

    private final Object mActivitySyncRoot = new Object();

    private Activity mActivity;
    private boolean mRunning = false;

    private FragmentLifecycleCallbacks mCallbacks;

    private Set<Fragment> mFragmentsInLastPass = new HashSet<Fragment>();

    private Field mAddedField;
    private Field mActiveField;

    public FragmentLifecycleWatcher(Activity activity, FragmentLifecycleCallbacks callbacks) {
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

    private Set<Fragment> findFragments(Activity activity) {
        cacheFields();
        if (mActiveField == null || mAddedField == null) {
            return new HashSet<Fragment>();
        }

        FragmentManager fm = activity.getFragmentManager();

        List<Fragment> active = getActiveFragments(fm);
        List<Fragment> added = getAddedFragments(fm);

        int size = 0;
        size += active != null ? active.size() : 0;
        size += added != null ? added.size() : 0;

        Set<Fragment> fragments = new HashSet<Fragment>(size);

        if (active != null) {
            fragments.addAll(active);
        }
        if (added != null) {
            fragments.addAll(added);
        }

        return fragments;
    }

    private void cacheFields() {
        if (mActiveField != null && mAddedField != null) {
            return;
        }

        try {
            Class fmi = Class.forName("android.app.FragmentManagerImpl");

            mActiveField = fmi.getDeclaredField("mActive");
            mActiveField.setAccessible(true);

            mAddedField = fmi.getDeclaredField("mAdded");
            mAddedField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Fragment> getActiveFragments(FragmentManager fm) {
        try {
            return (ArrayList<Fragment>) mActiveField.get(fm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<Fragment>();
        }
    }

    private List<Fragment> getAddedFragments(FragmentManager fm) {
        try {
            return (ArrayList<Fragment>) mAddedField.get(fm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<Fragment>();
        }
    }

    @Override
    public void run() {
        while (mRunning) {
            synchronized (mActivitySyncRoot) {
                Set<Fragment> fragments = findFragments(mActivity);
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
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
