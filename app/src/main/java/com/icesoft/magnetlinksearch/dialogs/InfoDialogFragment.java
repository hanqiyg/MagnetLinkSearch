package com.icesoft.magnetlinksearch.dialogs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.FileAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

public class InfoDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = "TestDialogFragment";
    @BindView(R.id.files)
    RecyclerView files;

    @BindView(R.id.tv_date)  TextView date;
    @BindView(R.id.tv_name)  TextView name;
    @BindView(R.id.tv_size)  TextView size;
    @BindView(R.id.tv_count) TextView count;

    @BindView(R.id.share)   ImageView share;
    @BindView(R.id.fav)     ImageView fav;
    @BindView(R.id.down)    ImageView down;
    @BindView(R.id.cancel)  ImageView cancel;

    @BindView(R.id.custom_title)   TextView title;
    @BindView(R.id.custom_close)   ImageView close;


    FileAdapter adapter;
    ResultWithFiles r = null;
    ResultDao dao;
    @Override
    int getLayoutResourceID() {
        return R.layout.dialog_info;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {
        adapter = new FileAdapter();
        files.setLayoutManager(new LinearLayoutManager(mActivity));
        files.setAdapter(adapter);
        files.addItemDecoration(new SpacesItemDecoration(0,4,0,4));
        title.setText(mActivity.getString(R.string.info_title));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialogFragment.this.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialogFragment.this.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(r));
                sendIntent.setType("text/plain");
                mActivity.startActivity(sendIntent);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.setFav(fav,getDao().set(r));
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(r != null && r.id != null){
                    Intent intent = new Intent();
                    Uri content_url = Uri.parse(FormatUtils.magnetFromId(r.id));
                    intent.setData(content_url);
                    mActivity.startActivity(intent);
                }
            }
        });
    }
    public ResultDao getDao(){
        if(dao == null){
            dao = new ResultDao(mActivity);
        }
        return dao;
    }

    @Override
    void initData() {
        String id = SharedPreferencesUtils.readDocumentId(mActivity);
        if(id != null){
            String json = String.format(Constance.ID_SEARCH,id);
            ElasticRestClient.post(mActivity, Constance.PATH,json,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    r = Utils.JSON2ResultWithFiles(response);
                    if(r != null){
                        date.setText(FormatUtils.formatDate(r.date));
                        name.setText(FormatUtils.htmlText(r.name));
                        size.setText(FormatUtils.formatSize(r.size));
                        count.setText(String.valueOf(r.count) + " item(s)");
                        adapter.setData(r.files);
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(FRAGMENT_TAG, responseString);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(FRAGMENT_TAG, errorResponse != null ? errorResponse.toString() : "null");
                }
            });
        }
    }

    @Override
    protected void refreshData() {

    }
    public static InfoDialogFragment newInstance(Bundle bundle){
        InfoDialogFragment fragment = new InfoDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
