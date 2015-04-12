package com.nillerr.fragmentlifecyclecallbacks;

import android.support.v4.app.Fragment;

public interface SupportFragmentLifecycleCallbacks {

    void onAdded(Fragment fragment);

    void onRemoved(Fragment fragment);

}
