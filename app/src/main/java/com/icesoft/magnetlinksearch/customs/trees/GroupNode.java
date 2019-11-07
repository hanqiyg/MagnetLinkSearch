package com.icesoft.magnetlinksearch.customs.trees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.utils.TreeUtils;
import com.unnamed.b.atv.model.TreeNode;

public class GroupNode extends TreeNode.BaseNodeViewHolder<String> {
    public GroupNode(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.tree_node_group, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value);
        TreeUtils.TreeNodePaddingLeft(context,view,node);
        return view;
    }
}
