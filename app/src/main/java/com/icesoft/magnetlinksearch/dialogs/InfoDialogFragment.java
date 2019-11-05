package com.icesoft.magnetlinksearch.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.FileAdapter;
import com.icesoft.magnetlinksearch.customs.SpacesItemDecoration;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.icesoft.magnetlinksearch.utils.SharedPreferencesUtils;
import com.icesoft.magnetlinksearch.utils.Utils;
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
    @BindView(R.id.file)    ImageView file;
    @BindView(R.id.down)    ImageView down;

    @BindView(R.id.no)       TextView no;
    @BindView(R.id.total)   TextView total;

    FileAdapter adapter;

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
    }

    @Override
    void initData() {
        String id = SharedPreferencesUtils.readDocumentId(mActivity);
        if(id != null){
            String json = String.format(Constance.ID_SEARCH,id);
            ElasticRestClient.post(mActivity, Constance.PATH,json,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    ResultWithFiles r = Utils.JSON2ResultWithFiles(response);
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
