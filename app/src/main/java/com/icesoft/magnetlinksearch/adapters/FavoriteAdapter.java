package com.icesoft.magnetlinksearch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.fragments.FavoriteFragment;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.VH>{
    private static final String FRAGMENT_TAG = FavoriteAdapter.class.getSimpleName();
    private Context context;
    private FavoriteFragment fragment;
    private List<Result> results = new ArrayList<>();
    private int total;
    private int from;
    private ResultDao dao;

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
        Result r = results.get(position);
        v.date.setText(FormatUtils.formatDate(r.date));
        v.name.setText(FormatUtils.htmlText(r.name));
        v.size.setText(FormatUtils.formatSize(r.size));
        v.files.setText(r.count + context.getString(R.string.file_unit));
        v.no.setText(String.valueOf(position + 1));
        v.total.setText(String.valueOf(total));

        ViewUtils.setfav(v.fav,getDao().exist(r.id));
        v.share.setOnClickListener(view -> {ViewUtils.share(context,r);});
        v.fav.setOnClickListener(view -> {  ViewUtils.fav(context,r,getDao(),v.fav,this,position);});
        v.file.setOnClickListener(view ->   ViewUtils.file(context,r));
        v.down.setOnClickListener(view -> { ViewUtils.down(context,r);});
    }
    @Override
    public int getItemCount() {
        return results==null?0:results.size();
    }
    public void refresh(List<Result> newData, int total){
        this.total = total;
        results.clear();
        if(newData != null && newData.size()>0){
            results.addAll(newData);
            from = results.size();
        }
        notifyDataSetChanged();
    }
    public void addData(List<Result> more) {
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
    public ResultDao getDao(){
        if(dao == null){
            dao = new ResultDao(context);
        }
        return dao;
    }
    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_date)     TextView date;
        @BindView(R.id.tv_name)     TextView name;
        @BindView(R.id.tv_size)     TextView size;
        @BindView(R.id.tv_count)    TextView files;
        @BindView(R.id.share)       ImageView share;
        @BindView(R.id.fav)         ImageView fav;
        @BindView(R.id.file)        ImageView file;
        @BindView(R.id.down)        ImageView down;
        @BindView(R.id.no)          TextView no;
        @BindView(R.id.total)       TextView total;

        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
