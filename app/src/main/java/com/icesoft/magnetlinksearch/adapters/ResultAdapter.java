package com.icesoft.magnetlinksearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private Context mActivity;
    private ResultFragment fragment;
    private List<Result> results = new ArrayList<>();
    private int total = 0;
    private int from = 0;
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
        this.mActivity = activity;
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
        ResultDao dao = new ResultDao(mActivity);
        Result r = results.get(position);
        v.date.setText(FormatUtils.formatDate(r.date));
        v.name.setText(FormatUtils.htmlText(r.name));
        v.size.setText(FormatUtils.formatSize(r.size));
        v.count.setText(String.valueOf(r.count) + " item(s)");
        v.no.setText(String.valueOf(position + 1));
        v.total.setText(String.valueOf(total));

        setFav(v.fav,dao.exist(r.id));
        v.share.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(r));
            sendIntent.setType("text/plain");
            mActivity.startActivity(sendIntent);
        });
        v.fav.setOnClickListener(view -> {
            setFav(v.fav,dao.set(r));
            notifyItemChanged(position);
        });
        v.file.setOnClickListener(view -> ViewUtils.gotoInfoDialogFragment(mActivity,r.id));
        v.down.setOnClickListener(view -> {
            Intent intent = new Intent();
            Uri content_url = Uri.parse(FormatUtils.magnetFromId(r.id));
            intent.setData(content_url);
            mActivity.startActivity(intent);
        });
    }
    public void setFav(ImageView fav, boolean b){
        if(b){
            fav.setImageResource(R.drawable.ic_favorite_red_24dp);
        }else{
            fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
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
