package com.icesoft.magnetlinksearch.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.ElasticUtils;

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
        if(r != null) {
            ElasticUtils.getFilesById(getActivity(),svFiles,r.id);
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
