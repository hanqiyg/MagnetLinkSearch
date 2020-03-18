package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.FileTreeDialogFragment;
import com.icesoft.magnetlinksearch.models.Magnet;

public class ViewUtils {
    public static void share(Context context, Magnet magnet){
        if(context != null && magnet != null){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(magnet));
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
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
            context.startActivity(intent);
        }
    }
    public static void fav(Context context, ImageView v, RecyclerView.Adapter adapter, int pos, Magnet magnet){
        App.getApp().getDao().set(magnet);
        showFav(v,magnet);
        adapter.notifyItemChanged(pos);
    }

    public static void showFav(ImageView v, Magnet magnet) {
        if(App.getApp().getDao().exist(magnet.getId())){
            v.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
        }else{
            v.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }
    }
}
