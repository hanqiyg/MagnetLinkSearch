package com.icesoft.magnetlinksearch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.events.BackPressedEvent;
import com.icesoft.magnetlinksearch.events.ExitEvent;
import com.icesoft.magnetlinksearch.events.NetworkChangeEvent;
import com.icesoft.magnetlinksearch.events.ShowFragmentEvent;
import com.icesoft.magnetlinksearch.recevicers.NetworkConnectChangedReceiver;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment extends Fragment {
    protected static final String KEY = "argments";
    Context context;
    private Bundle bundle;
    private View rootView;
    protected Unbinder unbinder;
    private NetworkConnectChangedReceiver mNetWorkChangReceiver;
    protected boolean isConnected = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getName(),"onAttach");
        this.context = context;
        bundle = getArguments();
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
        initNetworkListener();
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
        EventBus.getDefault().register(this);
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
            refreshData();
        }
    }
    public void initNetworkListener(){
        mNetWorkChangReceiver = new NetworkConnectChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mNetWorkChangReceiver, filter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNetworkStatus(NetworkChangeEvent event){
        ImageView networkImageView = rootView.findViewById(R.id.network_icon);
        if(event != null){
            if(event.isConnected){
                isConnected = true;
                if(event.networkType == ConnectivityManager.TYPE_WIFI){
                    if(networkImageView != null){networkImageView.setImageResource(R.drawable.network_wifi);}
                    Toast.makeText(context,getString(R.string.network_wifi),Toast.LENGTH_SHORT).show();
                }
                if(event.networkType == ConnectivityManager.TYPE_MOBILE){
                    if(networkImageView != null){networkImageView.setImageResource(R.drawable.network_mobile);}
                    Toast.makeText(context,getString(R.string.network_mobile),Toast.LENGTH_SHORT).show();
                }
                online();
            }else{
                isConnected = false;
                if(networkImageView != null){networkImageView.setImageResource(R.drawable.network_none);}
                Toast.makeText(context,getString(R.string.network_none),Toast.LENGTH_SHORT).show();
                offline();
            }
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
        EventBus.getDefault().unregister(this);
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
        context.unregisterReceiver(mNetWorkChangReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getName(),"onDetach");
    }
    public void online(){}
    public void offline(){}
    abstract int getLayoutResourceID();
    abstract String getName();
    abstract void initView();
    abstract void initData();
    protected abstract void refreshData();
    abstract String getBackStack();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackPress(BackPressedEvent event){
        if(this.isVisible()){
            if(getBackStack()==null){
                Log.d(getName(),"EXIT");
                EventBus.getDefault().post(new ExitEvent());
            }else{
                Log.d(getName(),"BackPressedEvent:" + getBackStack());
                EventBus.getDefault().post(new ShowFragmentEvent(getBackStack()));
            }
        }
    }
}
