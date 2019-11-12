package com.icesoft.magnetlinksearch.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.Constance;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.TreeUtils;
import com.icesoft.magnetlinksearch.utils.Utils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
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

    @BindView(R.id.tv_date)  TextView tvDate;
    @BindView(R.id.tv_name)  TextView tvName;
    @BindView(R.id.tv_size)  TextView tvSize;
    @BindView(R.id.tv_count) TextView tvCount;

    @BindView(R.id.share)   ImageView ivShare;
    @BindView(R.id.fav)     ImageView ivFav;
    @BindView(R.id.down)    ImageView ivDown;
    @BindView(R.id.cancel)  ImageView cancel;

    @BindView(R.id.custom_title)   TextView title;
    @BindView(R.id.custom_close)   ImageView close;

    @BindView(R.id.files)
    ScrollView svFiles;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.progress)
    ProgressBar progress;

    public Result r;

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
    }
    private Result getResult(Bundle bundle){
        if(bundle != null){
            String id = bundle.getString(ID);
            String date = bundle.getString(DATE);
            String name = bundle.getString(NAME);
            long size = bundle.getLong(SIZE);
            long count = bundle.getLong(COUNT);
            return new Result(id,date,name,size,count);
        }
        return null;
    }
    @Override
    void initData() {
        r = getResult(bundle);
        if(r != null){
            ViewUtils.setResultView(context,r,getDao(),null,-1,1,
                    tvDate,tvName,tvSize,tvCount,null,null,ivShare,ivFav,null,ivDown);
            ViewUtils.setProgress(svFiles,progress,message,ViewUtils.Status.Inprogress,getString(R.string.loading));
            String json = String.format(Constance.ID_SEARCH,r.id);
            ElasticRestClient.post(context, Constance.PATH,json,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    ResultWithFiles rf = Utils.JSON2ResultWithFiles(response);
                    if(rf != null){
                        TreeNode root = TreeUtils.createTree(context,rf.files);
                        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
                        svFiles.addView(tView.getView());
                        ViewUtils.setProgress(svFiles,progress,message,ViewUtils.Status.Success,getString(R.string.loaded));
                    }else{
                        ViewUtils.setProgress(svFiles,progress,message,ViewUtils.Status.Failure,getString(R.string.nodata));
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(FRAGMENT_TAG, errorResponse != null ? errorResponse.toString() : "null");
                    ViewUtils.setProgress(svFiles,progress,message,ViewUtils.Status.Failure,getString(R.string.network_error));
                }
            });
        }else{
            ViewUtils.setProgress(svFiles,progress,message,ViewUtils.Status.Failure,getString(R.string.error));
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
