package de.viadee.dv.model;


/**
 * Helper Class to determine the maximum depth of an hierarchy represented by an root {@link HierarchyTreeNode}. The
 * level map contains all levels and their related {@link HierarchyTreeNode}.
 * 
 * @author B01
 *
 */
public class Hierarchy {

    private HierarchyTreeNode root;

    private int maxHierDepth;

    public Hierarchy(HierarchyTreeNode root, int maxHierDepth) {
        super();
        this.root = root;
        this.maxHierDepth = maxHierDepth;
        // this.levelMap = new HashMap<Integer, List<HierarchyTreeNode>>();
    }

    public HierarchyTreeNode getRoot() {
        return root;
    }

    public void setRoot(HierarchyTreeNode root) {
        this.root = root;
    }

    public int getMaxHierDepth() {
        return maxHierDepth;
    }

    public void setMaxHierDepth(int maxHierDepth) {
        this.maxHierDepth = maxHierDepth;
    }

}
