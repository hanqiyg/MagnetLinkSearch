package com.icesoft.magnetlinksearch.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
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
    TreeNode root = TreeNode.root();
    public void createTree(List<TFile> files){
        for(TFile f : files){
            Log.d(FRAGMENT_TAG,"TFile:" + f.toString());
            String[] paths = f.name.split("/");
            Log.d(FRAGMENT_TAG,"TFile:" + paths.toString());
            if(paths.length==1){
                TreeNode node = new TreeNode(new Node(paths[0],f.length)).setViewHolder(new FileNode(mActivity));
                root.addChild(node);
                Log.d(FRAGMENT_TAG,"Root add:" + f.toString());
            }else{
                TreeNode parent = root;
                for (int i=0;i<paths.length-1;i++){
                    String name = paths[i];
                    Log.d(FRAGMENT_TAG,"for:" + name);
                    TreeNode current = getTreeNodeByName(name,parent);
                    if (current == null){
                        current = new TreeNode(paths[i]).setViewHolder(new GroupNode(mActivity));
                        parent.addChild(current);
                        parent = current;
                        Log.d(FRAGMENT_TAG,"create node:" + paths[i]);
                    }else{
                        parent = current;
                        Log.d(FRAGMENT_TAG,"use node:" + current.getValue());
                    }
                }
                TreeNode leaf = new TreeNode(new Node(paths[paths.length-1],f.length)).setViewHolder(new FileNode(mActivity));
                parent.addChild(leaf);
            }
        }
    }
    public TreeNode getTreeNodeByName(String name,TreeNode node){
        for(TreeNode n : node.getChildren()){
            if(n.getValue().equals(name)){
                return n;
            }
        }
        return null;
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
                        createTree(r.files);
                        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
                        files.addView(tView.getView());
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
    public static FileTreeDialogFragment newInstance(Bundle bundle){
        FileTreeDialogFragment fragment = new FileTreeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
