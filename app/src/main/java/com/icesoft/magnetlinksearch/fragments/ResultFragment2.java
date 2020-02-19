package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.ResultAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.dos.Response;
import com.icesoft.magnetlinksearch.dos.SearchFails;
import com.icesoft.magnetlinksearch.events.QueryEvent;
import com.icesoft.magnetlinksearch.utils.SearchUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ResultFragment2 extends BaseFragment implements OnRefreshListener, OnLoadMoreListener{
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
        //adapter = new ResultAdapter(context,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_space_left),getResources().getDimensionPixelSize(R.dimen.item_space_top),getResources().getDimensionPixelSize(R.dimen.item_space_right),getResources().getDimensionPixelSize(R.dimen.item_space_bottom)));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    void initData() {}

    @Override
    protected void refreshData() {}

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        SearchUtils.search(context,q,0);
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        SearchUtils.search(context,q,adapter.getFrom());
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateEvent(Response event){
        EventBus.getDefault().removeStickyEvent(event);
        Log.d(FRAGMENT_TAG,event.toString());
        switch(event.getCode()){
            case QueryList:{
                if(event.getPayload()!=null){
                    refreshLayout.finishRefresh(true);
                    ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Success,getString(R.string.loaded));
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    ViewUtils.setProgress(recyclerView, progress, message, ViewUtils.Status.Failure, getString(R.string.nodata));
                }
            }break;
            case QuerySingle:{

            }break;
            case ErrorServer:
            case ErrorUnknow:
            {
                refreshLayout.finishRefresh(false);
                ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.network_error));
            }break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateEvent(SearchFails event){
        EventBus.getDefault().removeStickyEvent(event);
        Log.d(FRAGMENT_TAG,event.toString());
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

    public static ResultFragment2 newInstance(Bundle bundle){
        ResultFragment2 fragment = new ResultFragment2();
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}
