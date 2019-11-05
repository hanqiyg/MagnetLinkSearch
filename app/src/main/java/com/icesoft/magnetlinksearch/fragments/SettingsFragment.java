package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import com.icesoft.magnetlinksearch.R;


public class SettingsFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "SettingsFragment";
    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_search;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {

    }

    @Override
    void initData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    public boolean onBackPressed() {
        if(mHandler != null){
            mHandler.showFragment(SearchFragment.FRAGMENT_TAG);
            return true;
        }
        return false;
    }
    public static SettingsFragment newInstance(Bundle bundle){
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
