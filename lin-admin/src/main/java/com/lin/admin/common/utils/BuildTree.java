package com.lin.admin.common.utils;

import com.lin.admin.common.model.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {

    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
        for (Tree<T> children : nodes) {
            String parentIdd = children.getParentId();
            if (parentIdd == null || "0".equals(parentIdd)) {
                topNodes.add(children);
                continue;
            }

            iterator(nodes, parentIdd, children);
        }

        Tree<T> root = new Tree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId("-1");
            root.setParentId("");
            root.setHasParent(false);
            root.setHasChildren(true);
            root.setChecked(true);
            root.setChildren(topNodes);
            root.setText("顶级节点");
            Map<String, Object> state = new HashMap<>();
            state.put("opened", true);
            root.setState(state);
        }
        return root;
    }

    public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
        for (Tree<T> children : nodes) {
            String parentIdd = children.getParentId();
            if (parentIdd == null || idParam.equals(parentIdd)) {
                topNodes.add(children);
                continue;
            }

            iterator(nodes, parentIdd, children);
        }
        return topNodes;
    }

    private static  <T> void iterator(List<Tree<T>> nodes, String pId, Tree<T> children) {
        for (Tree<T> parent : nodes) {
            String id = parent.getId();
            if (id != null && id.equals(pId)) {
                parent.getChildren().add(children);
                children.setHasParent(true);
                parent.setHasChildren(true);
                continue;
            }
        }
    }

}