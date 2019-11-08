package com.icesoft.magnetlinksearch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.ResultAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.events.QueryEvent;
import com.icesoft.magnetlinksearch.models.Query;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.Utils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ResultFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String FRAGMENT_TAG = "ResultFragment";
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.progress)
    ProgressBar progress;

    public String q;
    private ResultAdapter adapter;

    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_result;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {
        adapter = new ResultAdapter(mActivity,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.item_space_left),
                getResources().getDimensionPixelSize(R.dimen.item_space_top),
                getResources().getDimensionPixelSize(R.dimen.item_space_right),
                getResources().getDimensionPixelSize(R.dimen.item_space_bottom)
        ));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    void initData() {}

    @Override
    protected void refreshData() {}

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Inprogress,getString(R.string.loading));
        @SuppressLint("DefaultLocale")
        String json = String.format(Constance.CONTEXT_JSON,q,Constance.QUERY_FROM,Constance.QUERY_LIMIT);
        ElasticRestClient.post(mActivity,Constance.PATH,json,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Result> results = Utils.JSON2Result(response);
                int total = Utils.JSON2TotalCount(response);
                if(results != null && results.size()>0 && total > 0){
                    adapter.refresh(results,total);
                    refreshLayout.finishRefresh(true);
                    ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Success,getString(R.string.loaded));
                }else{
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.nodata));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(FRAGMENT_TAG,statusCode + ":" + (errorResponse!=null?errorResponse.toString():"null"));
                ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.network_error));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(FRAGMENT_TAG,statusCode + ":" + (responseString!=null?responseString:"null"));
                refreshLayout.finishRefresh(false);
                ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.network_error));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(FRAGMENT_TAG,statusCode + ":" + (errorResponse!=null?errorResponse.toString():"null"));
                refreshLayout.finishRefresh(false);
                ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.network_error));
            }
        });
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        @SuppressLint("DefaultLocale")
        String json = String.format(Constance.CONTEXT_JSON, q,adapter.getFrom(),Constance.QUERY_LIMIT);
        ElasticRestClient.post(mActivity, Constance.PATH, json, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Result> data = Utils.JSON2Result(response);
                if(data.size() > 0){
                    adapter.addData(data);
                    refreshLayout.finishLoadMore(true);
                }else{
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(FRAGMENT_TAG,"4" + responseString.toString());
                refreshLayout.finishLoadMore(false);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(FRAGMENT_TAG,"5" + errorResponse.toString());
                refreshLayout.finishLoadMore(false);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(FRAGMENT_TAG,"5" + errorResponse.toString());
                refreshLayout.finishLoadMore(false);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateEvent(QueryEvent event){
        Log.d(FRAGMENT_TAG,"onUpdateEvent");
        EventBus.getDefault().removeStickyEvent(event);
        Log.d(FRAGMENT_TAG,event.toString());
        if(event==null){
            ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.nodata));
        }else{
            if(q==null || !event.query.equals(q)){
                q = event.query;
                refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    public static ResultFragment newInstance(Bundle bundle){
        ResultFragment fragment = new ResultFragment();
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}
