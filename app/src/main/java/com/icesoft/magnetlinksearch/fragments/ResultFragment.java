package com.icesoft.magnetlinksearch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.ResultAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.models.Query;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.FileUtils;
import com.icesoft.magnetlinksearch.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ResultFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String FRAGMENT_TAG = "ResultFragment";
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty)
    TextView empty;
    public Query q;
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
    void initData() {
        hasData(true,null);
        Log.d(FRAGMENT_TAG,"" + (mActivity instanceof Context));
        q = (Query) FileUtils.readObject(mActivity,FRAGMENT_TAG);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void refreshData() {
        hasData(true,null);
        Log.d(FRAGMENT_TAG,"" + (mActivity instanceof Context));
        q = (Query) FileUtils.readObject(mActivity,FRAGMENT_TAG);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        q.from = 0;
        @SuppressLint("DefaultLocale")
        String json = String.format(Constance.CONTEXT_JSON,q.context,q.from,q.size);
        ElasticRestClient.post(mActivity,Constance.PATH,json,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Result> results = Utils.JSON2Result(response);
                int total = Utils.JSON2TotalCount(response);
                if(results != null && results.size()>0 && total > 0){
                    adapter.refresh(results,total);
                    refreshLayout.finishRefresh(true);
                    hasData(true,"");
                }else{
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    hasData(false,getString(R.string.nodata));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(FRAGMENT_TAG,responseString);
                refreshLayout.finishRefresh(false);
                hasData(false,getString(R.string.error));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(FRAGMENT_TAG,errorResponse!=null?errorResponse.toString():"null");
                refreshLayout.finishRefresh(false);
                hasData(false,getString(R.string.error));
            }
        });
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        @SuppressLint("DefaultLocale")
        String json = String.format(Constance.CONTEXT_JSON, q.context,q.from,q.size);
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
    @Override
    public boolean onBackPressed() {
        if(mHandler != null){
            mHandler.showFragment(SearchFragment.FRAGMENT_TAG);
            return true;
        }
        return false;
    }
    public static ResultFragment newInstance(Bundle bundle){
        ResultFragment fragment = new ResultFragment();
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    public void hasData(boolean has,String context){
        Log.d(FRAGMENT_TAG,has + ":" + context);
        if(has){
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            if(null!=context){
                empty.setText(context);
            }
        }
    }
}
