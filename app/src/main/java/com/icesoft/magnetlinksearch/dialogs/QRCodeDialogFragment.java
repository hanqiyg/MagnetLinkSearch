package com.icesoft.magnetlinksearch.dialogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.OnClick;
import com.icesoft.magnetlinksearch.MainActivity;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.tasks.QRCodeEncodeTask;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class QRCodeDialogFragment extends BaseDialogFragment implements QRCodeEncodeTask.Listener{
    public static final String FRAGMENT_TAG = QRCodeDialogFragment.class.getSimpleName();
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String COUNT = "count";

    String id,timestamp,name;
    long size;
    int count;

    @BindView(R.id.tv_time)  TextView tvTimestamp;
    @BindView(R.id.tv_name)  TextView tvName;
    @BindView(R.id.tv_size)  TextView tvSize;
    @BindView(R.id.tv_count) TextView tvCount;

    @Nullable
    @BindView(R.id.qrcode)
    ImageView qrcode;
    @Nullable
    @BindView(R.id.message)
    TextView message;
    @Nullable
    @BindView(R.id.progress)
    ProgressBar progress;

    @Nullable
    @BindView(R.id.layout_qr)
    RelativeLayout layoutQR;

    QRCodeEncodeTask task;

    @Override
    int getLayoutResourceID() {
        return R.layout.dialog_qrencode;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {
        Log.d(FRAGMENT_TAG,id==null?"null":id);
        if(progress != null) {progress.setVisibility(View.VISIBLE);}
        if(id!=null){
            task = new QRCodeEncodeTask(this);
            task.execute(id);
        }
        ViewUtils.setBrif(id,name,size,count,timestamp,null,tvName,tvSize,tvCount,tvTimestamp);
    }
    @Override
    void initData() {
        if(bundle != null){
            id = bundle.getString(ID);
            timestamp = bundle.getString(DATE);
            name = bundle.getString(NAME);
            size = bundle.getLong(SIZE);
            count = bundle.getInt(COUNT);
        }else {
            Log.d(FRAGMENT_TAG,"bundle null");
        }
    }

    @Override
    protected void refreshData() {}
    public static QRCodeDialogFragment newInstance(Bundle bundle){
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onDataSuccess(int event){
    }
    @Override
    public void setBitmap(Bitmap bitmap) {
        if(progress != null) {progress.setVisibility(View.GONE);}
        if(qrcode != null){
            qrcode.setVisibility(View.VISIBLE);
            qrcode.setImageBitmap(bitmap);
        }
    }
    @OnClick(R.id.tv_snapshot)
    public void snapshot(){
        if(context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;
            activity.requestPermission();
        }
        ViewUtils.snapshot(context,layoutQR,"test",id);
    }
}
