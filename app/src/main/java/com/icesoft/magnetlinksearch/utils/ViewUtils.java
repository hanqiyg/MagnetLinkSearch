package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.FileTreeDialogFragment;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ViewUtils {
    public enum Status{
        Success,Failure,Inprogress;
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
    public static void share(Context context, Result result){
        if(context != null && result != null){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(result));
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
/*            String URL = "/share";
            String jsonString = "{\"id\" : \"%s\"  , \"imei\" : 28}";
            ElasticRestClient.post(context,URL,jsonString,new JsonHttpResponseHandler());*/
        }
    }
    public static void fav(Context context, Result result, ResultDao dao, ImageView fav, RecyclerView.Adapter adapter, int position){
        if (context != null && result != null && dao != null && fav != null){
            setfav(fav,dao.set(result));
        }
        if(adapter != null && position >=0){
            adapter.notifyItemChanged(position);
        }
    }
    public static void setfav(ImageView fav,boolean b){
        if(b){
            fav.setImageResource(R.drawable.ic_favorite_red_24dp);
        }else{
            fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }
    public static void file(Context context, Result result){
        if(context != null && context instanceof AppCompatActivity && result != null){
            Bundle bundle = new Bundle();
            bundle.putString(FileTreeDialogFragment.ID,result.id);
            bundle.putString(FileTreeDialogFragment.DATE, result.date);
            bundle.putString(FileTreeDialogFragment.NAME, result.name);
            bundle.putLong(FileTreeDialogFragment.SIZE, result.size);
            bundle.putLong(FileTreeDialogFragment.COUNT, result.count);
            FileTreeDialogFragment dialog = FileTreeDialogFragment.newInstance(bundle);
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            dialog.show(manager,FileTreeDialogFragment.FRAGMENT_TAG);
        }
    }
    public static void down(Context context, Result result){
        if(context != null && result != null){
            Intent intent = new Intent();
            Uri content_url = Uri.parse(FormatUtils.magnetFromId(result.id));
            intent.setData(content_url);
            context.startActivity(intent);
        }
    }

    public static void setResultView(Context context,Result r,ResultDao dao,RecyclerView.Adapter adapter,int positon,int total,
                                     TextView tvDate,TextView tvName,TextView tvSize,TextView tvCount,TextView tvNo,TextView tvTotal,
                                     ImageView ivShare,ImageView ivFav,ImageView ivFile,ImageView ivDown
    ){
        if(r != null){
            if(tvDate != null)  { tvDate.setText(FormatUtils.formatDate(r.date)); }
            if(tvName != null)  { tvName.setText(FormatUtils.htmlText(r.name)); }
            if(tvSize != null)  { tvSize.setText(FormatUtils.formatSize(r.size)); }
            if(tvCount != null) { tvCount.setText(context.getString(R.string.file_unit,r.count)); }
            if(tvNo != null)    { tvNo.setText(String.valueOf(positon+1)); }
            if(tvTotal != null) { tvTotal.setText(String.valueOf(total)); }

            if(ivShare != null) { ivShare.setOnClickListener(View -> ViewUtils.share(context,r)); }
            if(ivFav != null)   {
                setfav(ivFav,dao.exist(r.id));
                ivFav.setOnClickListener(View -> ViewUtils.fav(context,r,dao,ivFav,adapter,positon));
            }
            if(ivFile != null)  { ivFile.setOnClickListener(View -> ViewUtils.file(context,r)); }
            if(ivDown != null)  { ivDown.setOnClickListener(View -> ViewUtils.down(context,r)); }
        }
    }
}
