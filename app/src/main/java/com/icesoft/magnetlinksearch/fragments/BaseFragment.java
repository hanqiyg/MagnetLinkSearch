package com.icesoft.magnetlinksearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements OnBackPressedListener {
    protected static final String KEY = "argments";
    Context mActivity;
    IHander mHandler;
    private Bundle bundle;
    private View rootView;
    protected Unbinder unbinder;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getName(),"onAttach");
        mActivity = context;
        bundle = getArguments();
        if (!(context instanceof IHander)) {
            throw new ClassCastException(
                    "Hosting Activity must implement IHandle");
        } else {
            this.mHandler = (IHander) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getName(),"onCreate");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(getName(),"onCreateView");
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutResourceID(),container,false);
        }
        unbinder = ButterKnife.bind(this,rootView);
        initView();
        initData();
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getName(),"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getName(),"onStart");
        mHandler.setBackPressListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getName(),"onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(getName(),"onHiddenChanged: " + (hidden?"Hide":"Show"));
        if(!hidden){
            mHandler.setBackPressListener(this);
            refreshData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getName(),"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getName(),"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(getName(),"onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getName(),"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getName(),"onDetach");
    }
    abstract int getLayoutResourceID();
    abstract String getName();
    abstract void initView();
    abstract void initData();
    protected abstract void refreshData();
    @Override
    public abstract boolean onBackPressed();
}
