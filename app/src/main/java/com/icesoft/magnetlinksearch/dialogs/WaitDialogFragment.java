package com.icesoft.magnetlinksearch.dialogs;

import android.os.Bundle;
import com.icesoft.magnetlinksearch.R;

public class WaitDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = WaitDialogFragment.class.getSimpleName();

    @Override
    int getLayoutResourceID() {
        return R.layout.dialog_wait;
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
    protected void refreshData() {}
    public static WaitDialogFragment newInstance(Bundle bundle){
        WaitDialogFragment fragment = new WaitDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
