package com.icesoft.magnetlinksearch.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.utils.DeviceInfoUtils;
import com.icesoft.magnetlinksearch.utils.PermissionUtils;

public class InfoFragment extends BaseFragment{
    public static final String FRAGMENT_TAG = "InfoFragment";
    @BindView(R.id.phone_producer)  TextView phoneProducer;
    @BindView(R.id.phone_model)     TextView phoneModel;
    @BindView(R.id.system_version)  TextView systemVersion;
    @BindView(R.id.sdk_version)     TextView sdkVersion;
    @BindView(R.id.app_version_code)TextView appVersionCode;
    @BindView(R.id.app_version_name)TextView appVersionName;
    @BindView(R.id.phone_imei)      TextView phoneImei;
    @BindView(R.id.wifi_mac)        TextView wifiMac;

    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_phone_info;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    void initView() {
        phoneProducer   .setText("PhoneProducer:" + DeviceInfoUtils.getPhoneProducer());
        phoneModel      .setText("PhoneModel:" + DeviceInfoUtils.getPhoneModel());
        systemVersion   .setText("SystemVersion:" + DeviceInfoUtils.getSystemVersion());
        sdkVersion      .setText("SDK Version:" + Build.VERSION.SDK_INT);
        appVersionCode  .setText("App Version Code:" + DeviceInfoUtils.getAppVersionCode(context));
        appVersionName  .setText("App Version Name:" + DeviceInfoUtils.getAppVersionName(context));
        phoneImei       .setText("IMEI:" + DeviceInfoUtils.getIMEI(context));
        wifiMac         .setText("WIFI MAC:" + DeviceInfoUtils.getMAC());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }else{
            phoneImei.setText("IMEI:" + DeviceInfoUtils.getIMEI(context));
        }
    }

    @Override
    void initData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    public static InfoFragment newInstance(String text){
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }
    String imei = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(FRAGMENT_TAG,"onRequestPermissionsResult granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                     phoneImei.setText("IMEI:" + DeviceInfoUtils.getIMEI(context));
                } else {
                    Log.i(FRAGMENT_TAG,"onRequestPermissionsResult denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private static final int CODE_PERMISSIONS_REQUEST_READ_PHONE_STATE = 200;
    private void requestPermission() {
        AppCompatActivity activity = (AppCompatActivity) context;
        Log.i(FRAGMENT_TAG,"requestPermission");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(FRAGMENT_TAG,"checkSelfPermission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_PHONE_STATE)) {
                Log.i(FRAGMENT_TAG,"shouldShowRequestPermissionRationale");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        CODE_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            } else {
                Log.i(FRAGMENT_TAG,"requestPermissions");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        CODE_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
