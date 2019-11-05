package com.icesoft.magnetlinksearch.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.interfaces.OnQueryListener;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.KeybordUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import io.noties.markwon.Markwon;
import io.noties.markwon.html.HtmlPlugin;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "SearchFragment";
    private static final int MAX_CHARACTER = 15;
    @BindView(R.id.warn)
    TextView  tvWarn;
    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView  tvTitle;
    @BindView(R.id.quote)
    TextView  tvQuote;

    @BindView(R.id.tv_info)
    TextView  tvInfo;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.btn_search)
    Button btnSearch;

    @OnClick(R.id.btn_search)
    public void onClickBtnSearch(){
        search();
    }

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
        final Markwon markwon = Markwon.builder(mActivity).usePlugin(HtmlPlugin.create()).build();
        markwon.setMarkdown(tvTitle,getString(R.string.title));
        markwon.setMarkdown(tvQuote,getString(R.string.quote));
        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
            }
            return true;
        });
        etSearch.setEnabled(true);
        etSearch.setFocusable(true);
        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
        etSearch.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        int keep = MAX_CHARACTER - (dest.length() - (dend - dstart));
                        if(keep < (end - start)){
                            tvWarn.setText(getString(R.string.input_too_many,MAX_CHARACTER));
                            tvWarn.setVisibility(View.VISIBLE);
                            ivIcon.setVisibility(View.VISIBLE);
                            Toast.makeText(mActivity,"over 15",Toast.LENGTH_SHORT).show();
                        }else{
                            tvWarn.setText("");
                            tvWarn.setVisibility(View.INVISIBLE);
                            ivIcon.setVisibility(View.INVISIBLE);
                        }
                        if(keep <= 0){
                            return "";
                        }else if(keep >= end - start){
                            return null;
                        }else{
                            return source.subSequence(start,start + keep);
                        }
                    }
                }
        });
    }

    @Override
    void initData() {
        updateTotalCount();
    }

    @Override
    protected void refreshData() {
        updateTotalCount();
    }
    @Override
    public boolean onBackPressed() {
        return false;
    }
    public static SearchFragment newInstance(String text){
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void search(){
        if (etSearch.getText().length() > 0) {
            String keywords = new String(etSearch.getText().toString().toLowerCase());
            etSearch.clearFocus();
            KeybordUtil.closeKeybord((AppCompatActivity) mActivity);
            if(mActivity instanceof OnQueryListener){
                ((OnQueryListener)mActivity).Query(keywords,
                        Constance.QUERY_FROM,
                        Constance.QUERY_SIZE);
            }
        }
    }
    public void updateTotalCount(){
        ElasticRestClient.get("/test/_count",null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        long count = response.getLong("count");
                        tvInfo.setText(String.valueOf(count));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
