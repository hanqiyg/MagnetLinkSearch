package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.FileTreeDialogFragment;

public class ViewUtils {
    public enum Status{
        Success,Failure,Inprogress;
    }
    public static void gotoInfoDialogFragment(Context mActivity, String id){
        if(mActivity instanceof AppCompatActivity){
            Bundle bundle = new Bundle();
            bundle.putString(FileTreeDialogFragment.ID,id);
            FileTreeDialogFragment dialog = FileTreeDialogFragment.newInstance(bundle);
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
    public static void setProgress(View data, View progress, TextView tvMessage, Status status, String message){
        switch (status){
            case Inprogress: {
                data.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tvMessage.setText(message);
                tvMessage.setVisibility(View.VISIBLE);
            }break;
            case Success: {
                data.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tvMessage.setText(message);
                tvMessage.setVisibility(View.GONE);
            }break;
            case Failure: {
                data.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                tvMessage.setText(message);
                tvMessage.setVisibility(View.VISIBLE);
            }break;
        }
    }
}
