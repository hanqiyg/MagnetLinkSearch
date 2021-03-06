package com.icesoft.magnetlinksearch;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.multidex.MultiDex;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.sqlites.MagnetDAO;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.*;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

public class App extends Application {
    private static final String T = "App.class";
    private MagnetDAO dao;
    private ObjectMapper mapper;
    private static App app;
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    public static App getApp(){
        return app;
    }
    public MagnetDAO getDao(){
        if(dao == null){
            dao = new MagnetDAO(this);
        }
        return dao;
    }
    public ObjectMapper getMapper(){
        if(mapper == null){
            mapper = new ObjectMapper();
        }
        return mapper;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(T,"onCreate");
        app = this;
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(T,"onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(T,"onTrimMemory");
    }
    public static void toast(String message){
        Toast.makeText(app,message,Toast.LENGTH_SHORT).show();
    }
}
