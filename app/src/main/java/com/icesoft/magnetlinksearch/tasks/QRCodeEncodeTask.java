package com.icesoft.magnetlinksearch.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import com.icesoft.magnetlinksearch.R;

import java.lang.ref.WeakReference;

public class QRCodeEncodeTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<Listener> listener;
    public QRCodeEncodeTask(Listener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        if(listener.get()!=null && listener.get().getContext()!=null){
            Bitmap logoBitmap = BitmapFactory.decodeResource(listener.get().getContext().getResources(), R.mipmap.logo);
            return QRCodeEncoder.syncEncodeQRCode(strings[0], BGAQRCodeUtil.dp2px(listener.get().getContext(),
                    250), Color.BLACK, Color.WHITE, logoBitmap);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && listener != null && listener.get()!=null) {
            listener.get().setBitmap(bitmap);
            Log.d("success","");
            Toast.makeText(listener.get().getContext(), "生成带logo的英文二维码成功", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("failure","");
            Toast.makeText(listener.get().getContext(), "生成带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
        }
    }
    public interface Listener{
        Context getContext();
        void setBitmap(Bitmap bitmap);
    }
}
