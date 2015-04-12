package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Fragment;

public interface FragmentLifecycleCallbacks {

    void onAdded(Fragment fragment);

    void onRemoved(Fragment fragment);

}
