package de.viadee.dv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Node helps to represent hierarchies in {@link Dimension}s from hierarchical {@link Link}s. Every node has a list of
 * children and entry for its parent. The level-attribute saves information about the depth of an element in the overall
 * hierarchy. This information is needed to concatenate the DDL script for the hierarchy flattening.
 * 
 * @author B01
 *
 */
public class HierarchyTreeNode {

    private String sqn;

    private String parentSqn;

    private HierarchyTreeNode parent;

    private List<HierarchyTreeNode> childs;

    private int level;

    public HierarchyTreeNode() {
        super();
        this.childs = new ArrayList<HierarchyTreeNode>();
    }

    public HierarchyTreeNode(String sqn, String parentSqn) {
        super();
        this.sqn = sqn;
        this.parentSqn = parentSqn;
        this.childs = new ArrayList<HierarchyTreeNode>();
    }

    public String getSqn() {
        return sqn;
    }

    public void setSqn(String sqn) {
        this.sqn = sqn;
    }

    public HierarchyTreeNode getParent() {
        return parent;
    }

    public void setParent(HierarchyTreeNode parent) {
        this.parent = parent;
    }

    public List<HierarchyTreeNode> getChilds() {
        return childs;
    }

    public void setChilds(List<HierarchyTreeNode> childs) {
        this.childs = childs;
    }

    public void addChild(HierarchyTreeNode child) {
        this.childs.add(child);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentSqn() {
        return parentSqn;
    }

    public void setParentSqn(String parentSqn) {
        this.parentSqn = parentSqn;
    }

    public boolean hasParent() {
        if (this.parent == null) {
            return false;
        }
        return true;
    }

    public boolean hasChildren() {
        if (this.childs.size() == 0) {
            return false;
        }
        return true;
    }

    public String toString() {

        String output = "SQN: " + this.sqn;
        if (this.hasParent()) {
            output = output + " PARENT" + this.getParent().getSqn();
        }
        output = output + " CHILDREN: ";
        for (HierarchyTreeNode child : this.getChilds()) {
            output = output + child.getSqn() + ", ";
        }
        return output;
    }
}
