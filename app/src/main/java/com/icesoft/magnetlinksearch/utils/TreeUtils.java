package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.customs.trees.FileNode;
import com.icesoft.magnetlinksearch.customs.trees.GroupNode;
import com.icesoft.magnetlinksearch.models.Node;
import com.icesoft.magnetlinksearch.models.TFile;
import com.unnamed.b.atv.model.TreeNode;

import java.util.List;

public class TreeUtils {
    private static final String T = TreeUtils.class.getSimpleName();
    public static TreeNode createTree(Context context,List<TFile> files){
        TreeNode root = TreeNode.root();
        for(TFile f : files){
            String[] paths = f.name.split("/");
            if(paths.length==1){
                TreeNode node = new TreeNode(new Node(paths[0],f.length)).setViewHolder(new FileNode(context));
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
                TreeNode leaf = new TreeNode(new Node(paths[paths.length-1],f.length)).setViewHolder(new FileNode(context));
                parent.addChild(leaf);
            }
        }
        return root;
    }
    public static TreeNode getTreeNodeByName(String name,int level,TreeNode root){
        for(TreeNode n : root.getChildren()){
            if(n.getLevel() == level && n.getValue().equals(name)){
                return n;
            }
        }
        return null;
    }
    public static void TreeNodePaddingLeft(Context context,View rootView,TreeNode node){
        int unit = context.getResources().getDimensionPixelSize(R.dimen.item_pading_tree);
        int padding = unit * node.getLevel();
        rootView.setPadding(padding,0,0,0);
    }
}
