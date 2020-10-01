package com.lin.admin.common.model;

import com.lin.admin.common.utils.JacksonUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Tree<T> {
    /**
     * 节点ID
     */
    private String id;
    /**
     * 显示节点文本
     */
    private String text;
    /**
     * 节点状态，open closed
     */
    private Map<String, Object> state;
    /**
     * 节点是否被选中 true false
     */
    private boolean checked = false;
    /**
     * 节点属性
     */
    private Map<String, Object> attributes;

    /**
     * 节点的子节点
     */
    private List<Tree<T>> children = new ArrayList<Tree<T>>();

    /**
     * 父ID
     */
    private String parentId;
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;

    public Tree(String id, String text, Map<String, Object> state, boolean checked, Map<String, Object> attributes,
                List<Tree<T>> children, boolean isParent, boolean isChildren, String parentID) {
        super();
        this.id = id;
        this.text = text;
        this.state = state;
        this.checked = checked;
        this.attributes = attributes;
        this.children = children;
        this.hasParent = isParent;
        this.hasChildren = isChildren;
        this.parentId = parentID;
    }

    public Tree() {
        super();
    }

    @SneakyThrows
    @Override
    public String toString() {
        return JacksonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tree)) {
            return false;
        }
        Tree<?> tree = (Tree<?>) o;
        return isChecked() == tree.isChecked() &&
                isHasParent() == tree.isHasParent() &&
                isHasChildren() == tree.isHasChildren() &&
                Objects.equals(getId(), tree.getId()) &&
                Objects.equals(getText(), tree.getText()) &&
                Objects.equals(getState(), tree.getState()) &&
                Objects.equals(getAttributes(), tree.getAttributes()) &&
                Objects.equals(getChildren(), tree.getChildren()) &&
                Objects.equals(getParentId(), tree.getParentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getState(), isChecked(), getAttributes(), getChildren(), getParentId(), isHasParent(), isHasChildren());
    }
}