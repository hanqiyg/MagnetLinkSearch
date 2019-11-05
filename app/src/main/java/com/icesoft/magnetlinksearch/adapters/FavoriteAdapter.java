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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.fragments.FavoriteFragment;
import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.sqlites.ResultDao;
import com.icesoft.magnetlinksearch.utils.FileUtils;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.icesoft.magnetlinksearch.utils.ViewUtils;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.VH>{
    private static final String FRAGMENT_TAG = "FavoriteAdapter";
    private Context mActivity;
    private FavoriteFragment fragment;
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
    public FavoriteAdapter(Context activity, FavoriteFragment fragment){
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
        Result r = fragment.favorite.results.get(position);
        //v.no.setText(String.format("%d / %d",position+1,fragment.q.total));
        v.date.setText(FormatUtils.formatDate(r.date));
        v.name.setText(FormatUtils.htmlText(r.name));
        v.size.setText(FormatUtils.formatSize(r.size));
        v.files.setText(String.valueOf(r.count) + " item(s)");
        v.no.setText(String.valueOf(position + 1));
        v.total.setText(String.valueOf(fragment.favorite.total));

        setFav(v.fav,dao.exist(r.id));
        v.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.gotoInfoDialogFragment(mActivity,r.id);
            }
        });
        v.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, FormatUtils.shareText(r));
                sendIntent.setType("text/plain");
                mActivity.startActivity(sendIntent);
            }
        });
        v.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFav(v.fav,dao.set(r));
                notifyItemChanged(position);
            }
        });
        v.file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.gotoInfoDialogFragment(mActivity,r.id);
            }
        });
        v.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Uri content_url = Uri.parse(FormatUtils.magnetFromId(r.id));
                intent.setData(content_url);
                mActivity.startActivity(intent);
            }
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
        return fragment.favorite.results==null?0:fragment.favorite.results.size();
    }
    public void refresh(List<Result> newData, int total){
        fragment.favorite.total = total;
        fragment.favorite.results.clear();
        if(newData != null && newData.size()>0){
            fragment.favorite.results.addAll(newData);
            fragment.favorite.from = fragment.favorite.results.size();
        }else{
            fragment.favorite.nodata = true;
        }
        FileUtils.writeObject(mActivity, FavoriteFragment.FRAGMENT_TAG,fragment.favorite);
        notifyDataSetChanged();
    }

    public void addData(List<Result> more) {
        int position = fragment.favorite.results.size();
        fragment.favorite.results.addAll(position, more);
        fragment.favorite.from = fragment.favorite.results.size();
        FileUtils.writeObject(mActivity,FavoriteFragment.FRAGMENT_TAG,fragment.favorite);
        notifyItemInserted(position);
    }
}
