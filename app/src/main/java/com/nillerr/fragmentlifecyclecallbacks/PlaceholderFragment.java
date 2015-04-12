package com.nillerr.fragmentlifecyclecallbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    public final double mRandom = Math.random();

    private Callback mCallback;

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnChangeFragment = (Button) rootView.findViewById(R.id.btn_change_fragment);
        btnChangeFragment.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        mCallback.onChangeFragment();
    }

    interface Callback {
        void onChangeFragment();
    }
}
