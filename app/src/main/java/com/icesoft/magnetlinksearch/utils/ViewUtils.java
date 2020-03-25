package com.icesoft.magnetlinksearch.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.adapters.FavoriteAdapter;
import com.icesoft.magnetlinksearch.dialogs.FileTreeDialogFragment;
import com.icesoft.magnetlinksearch.dialogs.QRCodeDialogFragment;
import com.icesoft.magnetlinksearch.mappers.MFile;
import com.icesoft.magnetlinksearch.models.Magnet;
import cz.msebera.android.httpclient.util.Asserts;

import java.util.List;

public class ViewUtils {
    private static final String EMPTY = "";
    public static void share(Context context, Magnet magnet){
        if(context instanceof AppCompatActivity && magnet != null){
            Bundle bundle = new Bundle();
            bundle.putString(QRCodeDialogFragment.ID,magnet.getId());
            bundle.putString(QRCodeDialogFragment.NAME,magnet.getName());
            bundle.putLong(QRCodeDialogFragment.SIZE,magnet.getLength());
            bundle.putInt(QRCodeDialogFragment.COUNT,magnet.getCount());
            bundle.putString(QRCodeDialogFragment.DATE,magnet.getTimestamp());
            QRCodeDialogFragment dialog = QRCodeDialogFragment.newInstance(bundle);
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            dialog.show(manager,FileTreeDialogFragment.FRAGMENT_TAG);
        }
    }
    public static void file(Context context, Magnet magnet){
        if(context instanceof AppCompatActivity && magnet != null){
            Bundle bundle = new Bundle();
            bundle.putString(FileTreeDialogFragment.ID,magnet.getId());
            bundle.putString(FileTreeDialogFragment.DATE, magnet.getTimestamp());
            bundle.putString(FileTreeDialogFragment.NAME, magnet.getName());
            bundle.putLong(FileTreeDialogFragment.SIZE, magnet.getLength());
            bundle.putInt(FileTreeDialogFragment.COUNT, magnet.getCount());
            if(magnet.getFiles()!=null){
                String json = JsonUtils.FilesToJsonString(magnet.getFiles());
                bundle.putString(FileTreeDialogFragment.FILES,json);
            }
            FileTreeDialogFragment dialog = FileTreeDialogFragment.newInstance(bundle);
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            dialog.show(manager,FileTreeDialogFragment.FRAGMENT_TAG);
        }
    }
    public static void down(Context context, Magnet magnet){
        if(context != null && magnet != null){
            Intent intent = new Intent();
            Uri content_url = Uri.parse(FormatUtils.magnetFromId(magnet.getId()));
            intent.setData(content_url);
            try{
                context.startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(context,"No magnet download App found.",Toast.LENGTH_SHORT).show();
            }
         }
    }
    public static void showFav(ImageView v, boolean isfav) {
        if(isfav){
            v.setImageResource(R.drawable.ic_favorite_red_24dp);
        }else{
            v.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }
    public static void setBrif(Magnet magnet, TextView id,TextView name,TextView length, TextView count, TextView timestamp){
        setBrif(magnet.getId(),magnet.getName(),magnet.getLength(),magnet.getCount(),magnet.getTimestamp(),
                id,name,length,count,timestamp);
    }
    public static void setBrif(String idString,String nameString,long len,int cou,String timeString,
                               TextView id,TextView name,TextView length, TextView count, TextView timestamp){
        setId(idString,id);
        setName(nameString,name);
        setTotalSize(len,length);
        setTotalCount(cou,count);
        setTimestamp(timeString,timestamp);
    }
    private static void setId(String s,TextView tv){
        if(tv != null)    {tv.setText(s==null?EMPTY:s);}
    }
    private static void setName(String s, TextView tv){
        if(tv != null){ tv.setText(s==null?EMPTY:s);}
    }
    private static void setTotalSize(long length, TextView tv){
        if(tv != null){ tv.setText(FormatUtils.formatSize(length));}
    }
    private static void setTotalCount(long count, TextView tv){
        if(tv != null){ tv.setText(String.valueOf(count));}
    }
    private static void setTimestamp(String s, TextView tv){
        if(tv != null){ tv.setText(s==null?EMPTY:FormatUtils.formatDate(s));}
    }
    public static void setButton(Magnet magnet, Context context,ImageView share,ImageView file,ImageView down,ImageView email){
        if(magnet!=null){
            if(share!=null) {share.setOnClickListener(v -> share(context,magnet));}
            if(file!=null)  {file.setOnClickListener(v -> file(context,magnet));}
            if(down!=null)  {down.setOnClickListener(v -> down(context,magnet));}
            if(email!=null) {email.setOnClickListener(v -> EmailUtils.email(context,magnet));}
        }
    }
    public static void snapshot(Context context,View view,String title,String description){
        Bitmap bitmap = BitmapUtils.getBitmapFromView(view);
        if(bitmap != null){
            String path = BitmapUtils.saveImageToGallery(context,bitmap,title,description);
            if(path != null){
                Toast.makeText(context,"snapshot[" + path +"]",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"snapshot is not succeeded.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static void setPage(int position, TextView no, int all, TextView to) {
        if(no!=null){no.setText(String.valueOf(position+1));}
        if(to!=null){to.setText(String.valueOf(all));}
    }

    public static void setFav(Magnet magnet, Context context, ImageView fav, RecyclerView.Adapter adapter, int position) {
        if(magnet == null){ return; }
        if(fav != null){ fav.setOnClickListener(v -> {
            boolean isfav = App.getApp().getDao().set(magnet);
            showFav(fav,isfav);
            notifyFav(isfav,magnet);
            if(adapter!=null){adapter.notifyItemChanged(position);}
        });}
    }
    private static void notifyFav(boolean is, Magnet magnet) {
        if(is){
            Toast.makeText(App.getApp(),"Add Favorite",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(App.getApp(),"Remove Favorite",Toast.LENGTH_SHORT).show();
        }
    }
}
