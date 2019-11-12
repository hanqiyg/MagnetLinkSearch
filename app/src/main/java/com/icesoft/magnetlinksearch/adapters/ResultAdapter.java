package com.icesoft.magnetlinksearch.adapters;

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
import com.icesoft.magnetlinksearch.fragments.ResultFragment;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.VH>{
    private static final String T = ResultAdapter.class.getSimpleName();
    private Context context;
    private ResultFragment fragment;
    private List<Result> results = new ArrayList<>();
    private int total = 0;
    private int from = 0;
    private ResultDao dao;
    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_date)  TextView date;
        @BindView(R.id.tv_name)  TextView name;
        @BindView(R.id.tv_size)  TextView size;
        @BindView(R.id.tv_count) TextView count;

        @BindView(R.id.share)   ImageView share;
        @BindView(R.id.fav)     ImageView fav;
        @BindView(R.id.file)    ImageView file;
        @BindView(R.id.down)    ImageView down;

        @BindView(R.id.no)       TextView no;
        @BindView(R.id.total)   TextView total;
        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    public ResultAdapter(Context activity, ResultFragment fragment){
        this.context = activity;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new VH(v);
    }

    private AlertDialog.Builder builder;
    @Override
    public void onBindViewHolder(@NonNull VH v, int position) {
        Result r = results.get(position);
        v.date.setText(FormatUtils.formatDate(r.date));
        v.name.setText(FormatUtils.htmlText(r.name));
        v.size.setText(FormatUtils.formatSize(r.size));
        v.count.setText(String.valueOf(r.count) + context.getString(R.string.file_unit));
        v.no.setText(String.valueOf(position + 1));
        v.total.setText(String.valueOf(total));

        ViewUtils.setfav(v.fav,dao.exist(r.id));
        v.share.setOnClickListener(view -> {    ViewUtils.share(context,r);});
        v.fav.setOnClickListener(view -> {      ViewUtils.fav(context,r,getDao(),v.fav,this,position);});
        v.file.setOnClickListener(view ->       ViewUtils.file(context,r));
        v.down.setOnClickListener(view ->       ViewUtils.down(context,r));
    }
    @Override
    public int getItemCount() {
        return results==null?0:results.size();
    }
    public void refresh(List<Result> newData, int total){
        if(newData != null && newData.size()>0){
            this.total = total;
            this.results.clear();
            this.results.addAll(newData);
            this.from = results.size();
            notifyDataSetChanged();
        }
    }
    public ResultDao getDao(){
        if(dao == null){
            dao = new ResultDao(context);
        }
        return dao;
    }

    public void addData(List<Result> more) {
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
