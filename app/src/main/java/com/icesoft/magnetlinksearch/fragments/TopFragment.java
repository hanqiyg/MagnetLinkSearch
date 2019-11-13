package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.EditText;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;

public class TopFragment extends BaseFragment{
    public static final String FRAGMENT_TAG = "TopFragment";
    public static final String URL = "https://www.163.com/";
    @BindView(R.id.webView)
    WebView webView;
    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_top;
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
        webView.loadUrl(URL);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    public static TopFragment newInstance(String text){
        TopFragment fragment = new TopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }
}
