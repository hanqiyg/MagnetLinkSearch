package com.icesoft.magnetlinksearch.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.events.FileTreeEvent;
import com.icesoft.magnetlinksearch.mappers.MFile;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.tasks.QRCodeEncodeTask;
import com.icesoft.magnetlinksearch.utils.ElasticUtils;
import com.icesoft.magnetlinksearch.utils.JsonUtils;
import com.icesoft.magnetlinksearch.utils.TreeUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;
import com.unnamed.b.atv.view.AndroidTreeView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class QRCodeDialogFragment extends BaseDialogFragment implements QRCodeEncodeTask.Listener{
    public static final String FRAGMENT_TAG = QRCodeDialogFragment.class.getSimpleName();
    public static final String ID = "MagnetId";
    @Nullable
    @BindView(R.id.qrcode)   ImageView qrcode;
    @Nullable
    @BindView(R.id.message)
    TextView message;
    @Nullable
    @BindView(R.id.progress)
    ProgressBar progress;
    String id;

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
        if(id!=null){
            task = new QRCodeEncodeTask(this);
            task.execute(id);
        }
    }
    @Override
    void initData() {
        if(bundle != null){
            id = bundle.getString(ID);
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
        progress.setVisibility(View.GONE);
        qrcode.setVisibility(View.VISIBLE);
        qrcode.setImageBitmap(bitmap);
    }
}
