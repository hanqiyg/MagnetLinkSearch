package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.icesoft.magnetlinksearch.dialogs.InfoDialogFragment;

public class ViewUtils {
    public static void gotoInfoDialogFragment(Context mActivity, String id){
        if(mActivity instanceof AppCompatActivity){
            SharedPreferencesUtils.writeDocumentId(mActivity,id);
            InfoDialogFragment dialog = InfoDialogFragment.newInstance(null);
            FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
            dialog.show(manager,InfoDialogFragment.FRAGMENT_TAG);
        }
    }
}
