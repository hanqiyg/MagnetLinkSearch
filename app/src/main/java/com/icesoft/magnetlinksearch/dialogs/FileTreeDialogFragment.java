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
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(r));
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
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
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    void initData() {
        if (bundle != null){
            String id = bundle.getString(ID);
            ViewUtils.setProgress(files,progress,message,ViewUtils.Status.Inprogress,getString(R.string.loading));
            String json = String.format(Constance.ID_SEARCH,id);
            ElasticRestClient.post(context, Constance.PATH,json,new JsonHttpResponseHandler() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    r = Utils.JSON2ResultWithFiles(response);
                    if(r != null){
                        date.setText(FormatUtils.formatDate(r.date));
                        name.setText(FormatUtils.htmlText(r.name));
                        size.setText(FormatUtils.formatSize(r.size));
                        count.setText(r.count + context.getString(R.string.file_unit));
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
