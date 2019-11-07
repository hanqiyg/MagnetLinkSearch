package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.FavoriteAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.models.Favorite;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.FileUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
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
    public Favorite favorite;
    private ResultDao dao;

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
        adapter = new FavoriteAdapter(mActivity,this);
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
    int getLayoutResourceID() {
        return R.layout.fragment_result;
    }

    @Override
    void initData() {
        favorite = (Favorite) FileUtils.readObject(mActivity,FRAGMENT_TAG);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void refreshData() {
        if(favorite != null && !favorite.nodata){
            initData();
        }
    }

    @Override
    public boolean onBackPressed() {
        if(mHandler != null){
            mHandler.showFragment(SearchFragment.FRAGMENT_TAG);
            return true;
        }
        return false;
    }
    public static FavoriteFragment newInstance(Bundle bundle){
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Inprogress,getString(R.string.loading));
        int total = getDao().count();
        if(total == 0){
            favorite.nodata = true;
            refreshLayout.finishLoadMoreWithNoMoreData();
            ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Failure,getString(R.string.nodata));
        }else{
            List<Result> results = getDao().load(0,favorite.limit);
            adapter.refresh(results,total);
            refreshLayout.finishRefresh(true);
            ViewUtils.setProgress(recyclerView,progress,message, ViewUtils.Status.Success,getString(R.string.loaded));
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        List<Result> results = getDao().load(favorite.from,favorite.limit);
        if(results != null && results.size()>0){
            adapter.addData(results);
            refreshLayout.finishLoadMore(true);
        }else{
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    private ResultDao getDao() {
        if(null == dao){
            dao = new ResultDao(mActivity);
        }
        return dao;
    }
}
