package de.viadee.dv.service.supplement.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hierarchy;
import de.viadee.dv.model.HierarchyTreeNode;
import de.viadee.dv.repository.impl.DimensionDAOImpl;
import de.viadee.dv.service.supplement.HierarchyFlattener;
import de.viadee.dv.sql.SupplementDDLCompositor;

public class HierarchyFlattenerImpl implements HierarchyFlattener {

    @Autowired
    private SupplementDDLCompositor supplementDDLCompositor;

    private static final Logger LOGGER = LogManager.getLogger(DimensionDAOImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void flattenHierarchyOfDimension(Dimension dim, String history) {
        if (dim.isHierarchical() && history.equals("false")) {
            // Create a new root Element to hold all information to build the hierarchy
            HierarchyTreeNode root = getHierarchyEntriesFromDimension(dim);
            LOGGER.debug("Hierarchy: " + root.toString());
            // Set tables hierarchy to new Hierarchy element
            Hierarchy hierarchy = new Hierarchy(root, 0);
            assembleNodeLevelsAndDetermineMax(root, hierarchy);
            String ddl = supplementDDLCompositor.ddlForHierarchyFlattening(dim, hierarchy);
            jdbcTemplate.execute(ddl);
            LOGGER.info("CREATED flat view for hierarchical dimension " + dim.getDimensionName());
        }
    }

    /**
     * Method to put the children of a node in relation. Furthermore, the new maximum depth of the overall hierarchy
     * will be determined. The hierarchies map over all levels and their descendant nodes will be filled.
     * 
     * @param node
     */
    private void assembleNodeLevelsAndDetermineMax(HierarchyTreeNode node, Hierarchy hierarchy) {
        if (node.hasChildren()) {
            for (HierarchyTreeNode child : node.getChilds()) {
                child.setLevel(child.getParent().getLevel() + 1);
                // Recursive call for child node to determine the childs children
                assembleNodeLevelsAndDetermineMax(child, hierarchy);
                // Determine the maximum depth of the whole hierarchy
                if (child.getLevel() > hierarchy.getMaxHierDepth()) {
                    hierarchy.setMaxHierDepth(child.getLevel());
                }
            }
        }
    }

    /**
     * Puts together the whole hierarchical (and bidirectional!) tree of {@link HierarchyTreeNode}s. This process is
     * needed to determine the total depth of the {@link Hierarchy} represented by the Root-{@link HierarchyTreeNode}
     * 
     * @param dim
     * @return root-{@link HierarchyTreeNode} that represent the overall hierarchy of the given hierarchical table
     */
    private HierarchyTreeNode getHierarchyEntriesFromDimension(Dimension dim) {
        String ddl = supplementDDLCompositor.dmlForSelectHierFieldsFromHierarchyDimension(dim
                .getDimensionName());

        // Final to be call-able in rowMapper
        final HashMap<String, HierarchyTreeNode> TEMP_MAP = new HashMap<String, HierarchyTreeNode>();

        // Read nodes from database and save their SQN and their PARENT_SQN
        List<HierarchyTreeNode> nodes = jdbcTemplate.query(ddl, new RowMapper<HierarchyTreeNode>() {

            public HierarchyTreeNode mapRow(ResultSet rs, int rowNum) throws SQLException {
                HierarchyTreeNode node = new HierarchyTreeNode(rs.getString(1), rs.getString(2));
                TEMP_MAP.put(rs.getString(1), node);
                return node;
            }
        });

        // Initiate new root element to represent the relations
        HierarchyTreeNode root = new HierarchyTreeNode("ROOT", null);
        root.setLevel(0);

        // Iterate over all found database entries that were previously saved within >nodes<
        for (HierarchyTreeNode node : nodes) {
            // if node has a parentSqn, the node will be equipped with the real parent object. The parent object will
            // get a new child
            if (node.getParentSqn() != null) {
                HierarchyTreeNode t_node = TEMP_MAP.get(node.getParentSqn());
                node.setParent(t_node);
                t_node.addChild(node);
            } else {
                // if parent_sqn is null, the element is part of the first level
                node.setParent(root);
                root.addChild(node);
            }
        }
        return root;
    }
}
