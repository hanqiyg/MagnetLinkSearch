package com.icesoft.magnetlinksearch.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.events.FileTreeEvent;
import com.icesoft.magnetlinksearch.mappers.MFile;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.utils.ElasticUtils;
import com.icesoft.magnetlinksearch.utils.JsonUtils;
import com.icesoft.magnetlinksearch.utils.TreeUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
import com.unnamed.b.atv.view.AndroidTreeView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FileTreeDialogFragment extends BaseDialogFragment {
    public static final String FRAGMENT_TAG = FileTreeDialogFragment.class.getSimpleName();
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String COUNT = "count";
    public static final String FILES = "files";

    @BindView(R.id.tv_date)  TextView tvDate;
    @BindView(R.id.tv_name)  TextView tvName;
    @BindView(R.id.tv_size)  TextView tvSize;
    @BindView(R.id.tv_count) TextView tvCount;

    @BindView(R.id.email)   ImageView ivEmail;
    @BindView(R.id.share)   ImageView ivShare;
    @BindView(R.id.fav)     ImageView ivFav;
    @BindView(R.id.down)    ImageView ivDown;
    @BindView(R.id.cancel)  ImageView cancel;

    @BindView(R.id.custom_title)   TextView title;
    @BindView(R.id.custom_close)   ImageView close;

    @BindView(R.id.files)
    ScrollView svFiles;
    @Nullable
    @BindView(R.id.message)
    TextView message;
    @Nullable
    @BindView(R.id.progress)
    ProgressBar progress;
    Magnet m;

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
    private Magnet getMagnet(Bundle bundle){
        Log.d(FRAGMENT_TAG,bundle.toString());
        if(bundle != null){
            String id = bundle.getString(ID);
            String timestamp = bundle.getString(DATE);
            String name = bundle.getString(NAME);
            long size = bundle.getLong(SIZE);
            int count = bundle.getInt(COUNT);
            String json = bundle.getString(FILES);
            List<MFile> files = null;
            if(json != null){files = JsonUtils.JsonStringToFiles(json);}
            return new Magnet(id,name,size,count,files,timestamp);
        }
        return null;
    }
    @Override
    void initData() {
        m = getMagnet(bundle);
        Log.d(FRAGMENT_TAG,m.toString());
        if(m != null) {
            ViewUtils.setBrif(m,null,tvName,tvSize,tvCount,tvDate);
            ViewUtils.showFav(ivFav, App.getApp().getDao().exist(m.getId()));
            ViewUtils.setFav(m,context,ivFav,null,0);
            ViewUtils.setButton(m,context,ivShare,null,ivDown,ivEmail);
            if(m.getFiles()!=null){
                Log.d(FRAGMENT_TAG,""+m.getFiles().size());
                AndroidTreeView tree = TreeUtils.getTreeView(getActivity(),m.getFiles());
                svFiles.addView(tree.getView());
            }else{
                ElasticUtils.getFilesById(getActivity(),m.getId());
            }
        }else{
            //取消等待
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onDataSuccess(FileTreeEvent event){
        AndroidTreeView tree = TreeUtils.getTreeView(getActivity(),event.files);
        svFiles.addView(tree.getView());
        EventBus.getDefault().removeStickyEvent(event);
    }
    private void showBrif(Magnet m){
        tvDate.setText(String.valueOf(m.getTimestamp()));
        tvCount.setText(String.valueOf(m.getCount()));
        tvName.setText(String.valueOf(m.getName()));
        tvSize.setText(String.valueOf(m.getLength()));
    }

    @Override
    protected void refreshData() {}
    public static FileTreeDialogFragment newInstance(Bundle bundle){
        FileTreeDialogFragment fragment = new FileTreeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
