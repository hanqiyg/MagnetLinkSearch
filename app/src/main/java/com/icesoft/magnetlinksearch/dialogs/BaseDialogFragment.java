package com.icesoft.magnetlinksearch.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.icesoft.magnetlinksearch.R;
import org.greenrobot.eventbus.EventBus;

public abstract class BaseDialogFragment extends DialogFragment {
    protected static final String KEY = "argments";
    Context context;
    protected Bundle bundle;
    private View rootView;
    @BindView(R.id.adView)
    protected AdView mAdView;
    protected Unbinder unbinder;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(getName(),"onCreateDialog");
        return super.onCreateDialog(savedInstanceState);
    }
    public void initAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

        initAds();
        initData();
        initView();
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
        Window mWindow = getDialog().getWindow();
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLayoutParams.width =   ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height =  ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.windowAnimations = R.style.BottomDialogAnimation;
        mWindow.setAttributes(mLayoutParams);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(getName(),"onSaveInstanceState");
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

}
