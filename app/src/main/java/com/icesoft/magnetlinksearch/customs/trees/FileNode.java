package com.icesoft.magnetlinksearch.customs.trees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.Node;
import com.icesoft.magnetlinksearch.utils.FormatUtils;
import com.unnamed.b.atv.model.TreeNode;

public class FileNode extends TreeNode.BaseNodeViewHolder<Node> {

    public FileNode(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Node value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.tree_node_file, null, false);
        TextView name = (TextView) view.findViewById(R.id.filename);
        name.setText(value.name);
        TextView size = (TextView) view.findViewById(R.id.filesize);
        size.setText(FormatUtils.formatSize(value.size));
        return view;
    }
}
