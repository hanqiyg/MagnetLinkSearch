package com.icesoft.magnetlinksearch.dialogs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

public class FileTreeDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = FileTreeDialogFragment.class.getSimpleName();
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String COUNT = "count";

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
        title.setText(context.getString(R.string.info_title));
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
                ViewUtils.share(context,r);
            }
        });
        ViewUtils.setfav(fav,dao.exist(r.id));
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.fav(context,r,getDao(),fav,null,-1);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.down(context,r);
            }
        });
    }

    @Override
    void initData() {
        if (bundle != null){
            String id = bundle.getString(ID);

            date.setText(FormatUtils.formatDate(bundle.getString(DATE)));
            name.setText(FormatUtils.htmlText(bundle.getString(NAME)));
            size.setText(FormatUtils.formatSize(bundle.getLong(SIZE)));
            count.setText(bundle.getLong(COUNT) + context.getString(R.string.file_unit));

            ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Inprogress,getString(R.string.loading));
            String json = String.format(Constance.ID_SEARCH,id);
            ElasticRestClient.post(context, Constance.PATH,json,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    r = Utils.JSON2ResultWithFiles(response);
                    if(r != null){
                        TreeNode root = TreeUtils.createTree(context,r.files);
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
            ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Failure,getString(R.string.error));
        }
    }

    public ResultDao getDao(){
        if(dao == null){
            dao = new ResultDao(context);
        }
        return dao;
    }

    @Override
    protected void refreshData() {}
    public static FileTreeDialogFragment newInstance(Bundle bundle){
        FileTreeDialogFragment fragment = new FileTreeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
