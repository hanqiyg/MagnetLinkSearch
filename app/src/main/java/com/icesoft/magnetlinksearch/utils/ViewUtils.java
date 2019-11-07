package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.FileTreeDialogFragment;
import com.icesoft.magnetlinksearch.dialogs.InfoDialogFragment;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;

public class ViewUtils {
    public static void gotoInfoDialogFragment(Context mActivity, String id){
        if(mActivity instanceof AppCompatActivity){
            SharedPreferencesUtils.writeDocumentId(mActivity,id);
            FileTreeDialogFragment dialog = FileTreeDialogFragment.newInstance(null);
            FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
            dialog.show(manager,FileTreeDialogFragment.FRAGMENT_TAG);
        }
    }
    public static void setFav(ImageView fav, boolean b){
        if(b){
            fav.setImageResource(R.drawable.ic_favorite_red_24dp);
        }else{
            fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }
}
