package com.icesoft.magnetlinksearch.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.customs.trees.FileNode;
import com.icesoft.magnetlinksearch.customs.trees.GroupNode;
import com.icesoft.magnetlinksearch.mappers.MFile;
import com.icesoft.magnetlinksearch.models.Node;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.List;
import java.util.Set;

public class TreeUtils {
    private static final String T = TreeUtils.class.getSimpleName();
    public static AndroidTreeView getTreeView(Activity activity, List<MFile> files){
        TreeNode root = createTree(activity,files);
        return new AndroidTreeView(activity, root);
    }
    private static TreeNode createTree(Context context,List<MFile> files){
        TreeNode root = TreeNode.root();
        for(MFile f : files){
            String[] paths = f.getName().split("/");
            if(paths.length==1){
                TreeNode node = new TreeNode(new Node(paths[0],f.getLength())).setViewHolder(new FileNode(context));
                root.addChild(node);
            }else{
                TreeNode parent = root;
                for (int i=0;i<paths.length-1;i++){
                    String name = paths[i];
                    TreeNode current = getTreeNodeByName(name,i+1,parent);
                    if (current == null){
                        current = new TreeNode(paths[i]).setViewHolder(new GroupNode(context));
                        parent.addChild(current);
                        parent = current;
                    }else{
                        parent = current;
                    }
                }
                TreeNode leaf = new TreeNode(new Node(paths[paths.length-1],f.getLength())).setViewHolder(new FileNode(context));
                parent.addChild(leaf);
            }
        }
        return root;
    }
    private static TreeNode getTreeNodeByName(String name,int level,TreeNode root){
        for(TreeNode n : root.getChildren()){
            if(n.getLevel() == level && n.getValue().equals(name)){
                return n;
            }
        }
        return null;
    }
    public static void TreeNodePaddingLeft(Context context, View rootView, TreeNode node){
        int unit = context.getResources().getDimensionPixelSize(R.dimen.item_pading_tree);
        int padding = unit * node.getLevel();
        rootView.setPadding(padding,0,0,0);
    }
}
