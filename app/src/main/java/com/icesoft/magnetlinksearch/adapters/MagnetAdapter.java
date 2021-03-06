package com.icesoft.magnetlinksearch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.fragments.ResultFragment;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class MagnetAdapter extends RecyclerView.Adapter<MagnetAdapter.VH>{
    private static final String T = MagnetAdapter.class.getSimpleName();
    private Context context;
    private ResultFragment fragment;
    private List<Magnet> results = new ArrayList<>();
    private int total = 0;
    private int from = 0;

    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_date)  TextView tvDate;
        @BindView(R.id.tv_name)  TextView tvName;
        @BindView(R.id.tv_size)  TextView tvSize;
        @BindView(R.id.tv_count) TextView tvCount;

        @BindView(R.id.share)   ImageView ivShare;
        @BindView(R.id.fav)     ImageView ivFav;
        @BindView(R.id.file)    ImageView ivFile;
        @BindView(R.id.down)    ImageView ivDown;
        @BindView(R.id.email)   ImageView ivEmail;

        @BindView(R.id.no)       TextView tvNo;
        @BindView(R.id.total)   TextView tvTotal;
        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    public MagnetAdapter(Context context, ResultFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new VH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull VH v, int position) {
        final Magnet m = results.get(position);
        if(m == null){return;}
        ViewUtils.setBrif(m,null,v.tvName,v.tvSize,v.tvCount,v.tvDate);
        ViewUtils.showFav(v.ivFav,App.getApp().getDao().exist(m.getId()));
        ViewUtils.setFav(m,context,v.ivFav,this,position);
        ViewUtils.setButton(m,context,v.ivShare,v.ivFile,v.ivDown,v.ivEmail);
        ViewUtils.setPage(position,v.tvNo,total,v.tvTotal);
    }
    @Override
    public int getItemCount() {
        return results==null?0:results.size();
    }
    public void refresh(List<Magnet> newData, int total){
        if(newData != null && newData.size()>0){
            this.total = total;
            this.results.clear();
            this.results.addAll(newData);
            this.from = results.size();
            notifyDataSetChanged();
        }
    }
    public void addData(List<Magnet> more) {
        if(more != null && more.size()>0){
            int position = results.size();
            this.results.addAll(position, more);
            this.from = results.size();
            notifyItemInserted(position);
        }
    }
    public int getTotal(){
        return total;
    }
    public int getFrom(){
        return from;
    }
}
