package com.icesoft.magnetlinksearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.TFile;
import com.icesoft.magnetlinksearch.utils.FormatUtils;


import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.VH>{
    private static final String T = "FileAdapter";
    private List<TFile> files;

    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)     TextView name;
        @BindView(R.id.tv_length)   TextView length;

        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new VH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull VH v, int position) {
        TFile file = files.get(position);
        v.name.setText(file.name);
        v.length.setText(FormatUtils.formatSize(file.length));
    }

    @Override
    public int getItemCount() {
        return files==null?0:files.size();
    }

    public void setData(List<TFile> files) {
        this.files = files;
        notifyDataSetChanged();
    }
}
