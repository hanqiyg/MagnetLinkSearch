package com.icesoft.magnetlinksearch.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.customs.trees.GroupNode;
import com.icesoft.magnetlinksearch.customs.trees.FileNode;
import com.icesoft.magnetlinksearch.models.Node;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.models.TFile;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.List;

public class FileTreeDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = "FileTreeDialogFragment";
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

    @BindView(R.id.files)
    ScrollView files;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.progress)
    ProgressBar progress;

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
        title.setText(mActivity.getString(R.string.info_title));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileTreeDialogFragment.this.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileTreeDialogFragment.this.dismiss();
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
        ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Inprogress,getString(R.string.loading));
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
                        TreeNode root = TreeUtils.createTree(mActivity,r.files);
                        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
                        files.addView(tView.getView());
                        ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Success,getString(R.string.loaded));
                    }else{
                        ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Failure,getString(R.string.nodata));
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(FRAGMENT_TAG, errorResponse != null ? errorResponse.toString() : "null");
                    ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Failure,getString(R.string.network_error));
                }
            });
        }else{
            ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Failure,getString(R.string.no_parameter));
        }
    }

    @Override
    protected void refreshData() {

    }
    public static FileTreeDialogFragment newInstance(Bundle bundle){
        FileTreeDialogFragment fragment = new FileTreeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
