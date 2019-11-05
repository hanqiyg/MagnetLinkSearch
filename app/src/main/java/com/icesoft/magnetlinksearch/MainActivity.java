package com.icesoft.magnetlinksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.icesoft.magnetlinksearch.fragments.*;
import com.icesoft.magnetlinksearch.interfaces.OnQueryListener;
import com.icesoft.magnetlinksearch.models.Favorite;
import com.icesoft.magnetlinksearch.models.Query;
import com.icesoft.magnetlinksearch.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnQueryListener,IHander {
    private static final String T = MainActivity.class.getSimpleName();
    private static final String LAST_FRAGMENT_TAG = "LAST_FRAGMENT_TAG";
    private static final String TAG = "test";
    private long back;
    private OnBackPressedListener backPressedListener;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initAds();
        mIntent();
        init(savedInstanceState);
    }
    public void initAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    public void mIntent(){
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(T,data==null?"data=null":data.toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG,backPressedListener==null?"null":backPressedListener.getTag());
            if(backPressedListener == null || !backPressedListener.onBackPressed()){
                long now = System.currentTimeMillis();
                if( now - back > 3000){
                    back = now;
                    Toast.makeText(this,R.string.click_twice,Toast.LENGTH_SHORT).show();
                }else{
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String tag = savedInstanceState.getString(LAST_FRAGMENT_TAG);
            showFragment(tag);
        }else{
            showFragment(SearchFragment.FRAGMENT_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_FRAGMENT_TAG, getLastFragmentTag());
    }

    private String getLastFragmentTag() {
        String lastFragmentTag = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment != null && fragment.isVisible()) {
                lastFragmentTag = fragment.getTag();
                break;
            }
        }
        return lastFragmentTag;
    }
    @Override
    public void showFragment(String tag){
        Fragment fragment = getFragment(tag);
        showFragment(fragment);
    }
    private Fragment getFragment(String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if(fragment == null){
            fragment = initFragment(tag);
            fragmentManager.beginTransaction().add(R.id.frame,fragment,tag).commit();
        }
        return fragment;
    }
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        for (Fragment f : fragmentList) {
            if (f != null && !f.equals(fragment)) {
                tx.hide(f);
            }
        }
        tx.show(fragment);
        tx.commit();
    }

    private Fragment initFragment(String tag) {
        Fragment fragment = null;
        switch (tag){
            case ResultFragment.FRAGMENT_TAG : fragment = ResultFragment.newInstance(null);break;
            case FavoriteFragment.FRAGMENT_TAG : fragment = FavoriteFragment.newInstance(null);break;
            case SettingsFragment.FRAGMENT_TAG : fragment = SettingsFragment.newInstance(null);break;
            default: fragment = SearchFragment.newInstance(null);break;
        }
        return fragment;
    }
    @Override
    public void Query(String queryString, int from, int size) {
        Query q = new Query(queryString,from,size,0,new ArrayList<>());
        FileUtils.writeObject(this,ResultFragment.FRAGMENT_TAG,q);
        showFragment(ResultFragment.FRAGMENT_TAG);
    }

    @Override
    public void setBackPressListener(OnBackPressedListener listener) {
        this.backPressedListener = listener;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Favorite f = new Favorite(Constance.FAVORITE_FROM,Constance.FAVORITE_LIMIT,0,new ArrayList<>());
            FileUtils.writeObject(this,FavoriteFragment.FRAGMENT_TAG,f);
            showFragment(FavoriteFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
