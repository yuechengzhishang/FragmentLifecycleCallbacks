package com.nillerr.fragmentlifecyclecallbacks;

import android.support.v4.app.Fragment;

public interface SupportFragmentLifecycleCallbacks {

    void onFragmentAdded(Fragment fragment);

    void onFragmentRemoved(Fragment fragment);

}
