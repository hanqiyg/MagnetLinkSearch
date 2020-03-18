package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.FavoriteAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.sqlites.MagnetDAO;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;


public class FavoriteFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String FRAGMENT_TAG = "FavoriteFragment";
    @BindView(R.id.refreshLayout)
    protected RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private MagnetDAO dao;

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {
        adapter = new FavoriteAdapter(context,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
    int getLayoutResourceID() {
        return R.layout.fragment_result;
    }

    @Override
    void initData() {
        refreshLayout.autoRefresh();
    }

    @Override
    protected void refreshData() {
        int total = App.getApp().getDao().count();
        if(total != adapter.getTotal()){
            refreshLayout.autoRefresh();
        }
    }

    @Override
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    public static FavoriteFragment newInstance(Bundle bundle){
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        int total = App.getApp().getDao().count();
        if(total == 0){
            refreshLayout.finishLoadMoreWithNoMoreData();
        }else{
            List<Magnet> results = App.getApp().getDao().load(Constance.FAVORITE_FROM, Constance.FAVORITE_LIMIT);
            adapter.refresh(results,total);
            refreshLayout.finishRefresh(true);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        List<Magnet> results = App.getApp().getDao().load(adapter.getFrom(),Constance.FAVORITE_LIMIT);
        if(results != null && results.size()>0){
            adapter.addData(results);
            refreshLayout.finishLoadMore(true);
        }else{
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }
}
