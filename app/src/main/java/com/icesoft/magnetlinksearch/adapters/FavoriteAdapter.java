package com.icesoft.magnetlinksearch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.fragments.FavoriteFragment;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.VH>{
    private static final String T = FavoriteAdapter.class.getSimpleName();
    private Context context;
    private FavoriteFragment fragment;
    private List<Magnet> results = new ArrayList<>();
    private int total;
    private int from;

    public FavoriteAdapter(Context context, FavoriteFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new VH(v);
    }

    private AlertDialog.Builder builder;
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VH v, int position) {
        final Magnet r = results.get(position);
        if(r == null){return;}
        v.tvName.setText(String.valueOf(r.getName()));
        v.tvSize.setText(String.valueOf(r.getLength()));
        v.tvCount.setText(String.valueOf(r.getCount()));
        v.tvDate.setText(String.valueOf(r.getTimestamp()));
        ViewUtils.showFav(v.ivFav,r);
        v.ivShare.setOnClickListener(view -> ViewUtils.share(context,r));
        v.ivFav.setOnClickListener(view ->  ViewUtils.fav(context,v.ivFav,this,position,r));
        v.ivFile.setOnClickListener(view -> ViewUtils.file(context,r));
        v.ivDown.setOnClickListener(view -> ViewUtils.down(context,r));
        v.tvNo.setText(String.valueOf(position));
        v.tvTotal.setText(String.valueOf(total));
    }
    @Override
    public int getItemCount() {
        return results==null?0:results.size();
    }
    public void refresh(List<Magnet> newData, int total){
        this.total = total;
        results.clear();
        if(newData != null && newData.size()>0){
            results.addAll(newData);
            from = results.size();
        }
        notifyDataSetChanged();
    }
    public void addData(List<Magnet> more) {
        int position = results.size();
        results.addAll(position, more);
        from = results.size();
        notifyItemInserted(position);
    }

    public int getFrom(){
        return from;
    }

    public int getTotal(){
        return total;
    }
    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_date)  TextView tvDate;
        @BindView(R.id.tv_name)  TextView tvName;
        @BindView(R.id.tv_size)  TextView tvSize;
        @BindView(R.id.tv_count) TextView tvCount;

        @BindView(R.id.share)   ImageView ivShare;
        @BindView(R.id.fav)     ImageView ivFav;
        @BindView(R.id.file)    ImageView ivFile;
        @BindView(R.id.down)    ImageView ivDown;

        @BindView(R.id.no)       TextView tvNo;
        @BindView(R.id.total)   TextView tvTotal;

        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
