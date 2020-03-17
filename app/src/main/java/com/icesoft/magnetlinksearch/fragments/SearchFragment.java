package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.customs.filters.TextLengthFilter;
import com.icesoft.magnetlinksearch.events.QueryEvent;
import com.icesoft.magnetlinksearch.events.ShowFragmentEvent;
import com.icesoft.magnetlinksearch.mappers.EsCount;
import com.icesoft.magnetlinksearch.utils.ElasticUtils;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.icesoft.magnetlinksearch.utils.KeybordUtil;
import com.icesoft.magnetlinksearch.utils.Utils;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import io.noties.markwon.Markwon;
import io.noties.markwon.html.HtmlPlugin;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class SearchFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "SearchFragment";
    private static final int MAX_CHARACTER = 15;
    @BindView(R.id.warn)
    TextView  tvWarn;
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
    @BindView(R.id.message)
    protected LinearLayout message;

    @OnClick(R.id.btn_search)
    public void onClickBtnSearch(){search();}

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
        final Markwon markwon = Markwon.builder(context).usePlugin(HtmlPlugin.create()).build();
        markwon.setMarkdown(tvTitle,getString(R.string.title));
        markwon.setMarkdown(tvQuote,getString(R.string.quote));
        btnSearch.setEnabled(false);
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
        etSearch.setFilters(new InputFilter[]{ new TextLengthFilter(context,message,tvWarn,btnSearch,3,15)});
    }

    @Override
    void initData() {
        updateTotalCount();
    }

    @Override
    protected void refreshData() {
        updateTotalCount();
    }
    public static SearchFragment newInstance(String text){
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void search(){
        String keywords = etSearch.getText().toString().trim();
        if (keywords.length() > 0) {
            etSearch.clearFocus();
            KeybordUtil.hideKeyboard((AppCompatActivity) context);
            String safe = Utils.secureKeywords(keywords);
            EventBus.getDefault().postSticky(new QueryEvent(safe));
            EventBus.getDefault().post(new ShowFragmentEvent(ResultFragment.FRAGMENT_TAG));
        }
    }
    public void updateTotalCount() {
        ElasticUtils.getMagnetCount(context,tvInfo);
    }

    @Override
    String getBackStack() {
        return null;
    }

    @Override
    public void online() {
        btnSearch.setEnabled(true);
    }

    @Override
    public void offline() {
        btnSearch.setEnabled(false);
    }
}
