package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import com.icesoft.magnetlinksearch.R;

public class ShareFragment extends BaseFragment{
    public static final String FRAGMENT_TAG = "ShareFragment";

    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_share;
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
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    public static ShareFragment newInstance(String text){
        ShareFragment fragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }
}
