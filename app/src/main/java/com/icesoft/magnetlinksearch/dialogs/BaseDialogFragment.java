package com.icesoft.magnetlinksearch.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.icesoft.magnetlinksearch.R;

public abstract class BaseDialogFragment extends DialogFragment {
    protected static final String KEY = "argments";
    Context mActivity;
    private Bundle bundle;
    private View rootView;
    protected Unbinder unbinder;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getName(),"onAttach");
        mActivity = (Activity) context;
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
        Window mWindow = getDialog().getWindow();
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLayoutParams.width =   ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height =  ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.windowAnimations = R.style.BottomDialogAnimation;
        mWindow.setAttributes(mLayoutParams);
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
