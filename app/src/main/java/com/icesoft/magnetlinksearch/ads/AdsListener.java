package com.icesoft.magnetlinksearch.ads;

import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdsListener extends AdListener {
    private String title;
    public AdsListener(String name) {
        super();
        title = "admob[" + name +"]";
        Log.d(title,"OnListenerCreate");
    }

    @Override
    public void onAdClosed() {
        Log.d(title,"OnAdClose");
    }

    @Override
    public void onAdFailedToLoad(int i) {
        Log.d(title,"OnAdFailedToLoad: " + i);
    }

    @Override
    public void onAdLeftApplication() {
        Log.d(title,"OnAdLeftApplication");
    }

    @Override
    public void onAdOpened() {
        Log.d(title,"OnAdOpened");
    }

    @Override
    public void onAdLoaded() {
        Log.d(title,"OnAdLoaded");
    }

    @Override
    public void onAdClicked() {
        Log.d(title,"OnAdClicked");
    }

    @Override
    public void onAdImpression() {
        Log.d(title,"OnAdImpression");
    }
}
