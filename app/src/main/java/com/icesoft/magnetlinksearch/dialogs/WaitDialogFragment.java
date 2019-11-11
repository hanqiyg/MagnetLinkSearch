package com.icesoft.magnetlinksearch.dialogs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

public class WaitDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = WaitDialogFragment.class.getSimpleName();

    @Override
    int getLayoutResourceID() {
        return R.layout.dialog_info;
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
