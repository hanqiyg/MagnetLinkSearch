package com.icesoft.magnetlinksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.icesoft.magnetlinksearch.events.BackPressedEvent;
import com.icesoft.magnetlinksearch.events.ExitEvent;
import com.icesoft.magnetlinksearch.events.ShowFragmentEvent;
import com.icesoft.magnetlinksearch.fragments.*;
import com.icesoft.magnetlinksearch.utils.KeybordUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String T = MainActivity.class.getSimpleName();
    private static final String LAST_FRAGMENT_TAG = "LAST_FRAGMENT_TAG";
    private long back;

    protected Unbinder unbinder;
    @BindView(R.id.adView)
    protected AdView mAdView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeybordUtil.hideKeyboard(MainActivity.this);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initAds();
        mIntent();
        init(savedInstanceState);
    }
    public void initAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(T,"onInitializationComplete");
            }
        });
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            EventBus.getDefault().post(new BackPressedEvent());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackPressedEvent(ExitEvent event) {
        long now = System.currentTimeMillis();
        if (now - back > 3000) {
            back = now;
            Toast.makeText(this, R.string.click_twice, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
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
            case ResultFragment.FRAGMENT_TAG    : fragment = ResultFragment.newInstance(null);break;
            case FavoriteFragment.FRAGMENT_TAG  : fragment = FavoriteFragment.newInstance(null);break;
            case SettingsFragment.FRAGMENT_TAG  : fragment = SettingsFragment.newInstance(null);break;
            case EmailFragment.FRAGMENT_TAG     : fragment = EmailFragment.newInstance(null);break;
            case TopFragment.FRAGMENT_TAG       : fragment = TopFragment.newInstance(null);break;
            case ShareFragment.FRAGMENT_TAG     : fragment = ShareFragment.newInstance(null);break;
            default: fragment = SearchFragment.newInstance(null);break;
        }
        return fragment;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchFragment(ShowFragmentEvent event){
        showFragment(event.tag);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_search) {
            showFragment(SearchFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_fav) {
            showFragment(FavoriteFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_slideshow) {
            showFragment(TopFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_manage) {
            showFragment(SettingsFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_share) {
            showFragment(ShareFragment.FRAGMENT_TAG);
        } else if (id == R.id.nav_send) {
            showFragment(EmailFragment.FRAGMENT_TAG);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
