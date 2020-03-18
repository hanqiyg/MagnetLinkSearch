package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.MagnetAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.events.QueryEvent;
import com.icesoft.magnetlinksearch.events.SearchFailEvent;
import com.icesoft.magnetlinksearch.events.SearchSuccessEvent;
import com.icesoft.magnetlinksearch.utils.ElasticUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ResultFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener{
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
    private MagnetAdapter adapter;

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
        adapter = new MagnetAdapter(getActivity(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_space_left),getResources().getDimensionPixelSize(R.dimen.item_space_top),getResources().getDimensionPixelSize(R.dimen.item_space_right),getResources().getDimensionPixelSize(R.dimen.item_space_bottom)));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    void initData() {

    }

    @Override
    protected void refreshData() {}

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ElasticUtils.stringSearch(context,q,0,10);
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ElasticUtils.stringSearch(context,q,adapter.getFrom(),10);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onQuery(QueryEvent event){
        this.q = event.query;
        refreshLayout.autoRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onSearchSuccessEvent(SearchSuccessEvent event){
        Log.d(FRAGMENT_TAG, "Update magnets:" + (event==null?0:event.magnets.size()));
        if(event.magnets != null && event.magnets.size()>0){
            if(event.refresh){
                adapter.refresh(event.magnets,event.total);
                refreshLayout.finishRefresh(true);
            }else{
                adapter.addData(event.magnets);
                refreshLayout.finishLoadMore(true);
            }
        }else{
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
        EventBus.getDefault().removeStickyEvent(event);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onSearchFailEvent(SearchFailEvent event){
        Log.d(FRAGMENT_TAG, "onSearchFailEvent:" + event.toString());
        Toast.makeText(context, event.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout.finishLoadMore(false);
        EventBus.getDefault().removeStickyEvent(event);
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
