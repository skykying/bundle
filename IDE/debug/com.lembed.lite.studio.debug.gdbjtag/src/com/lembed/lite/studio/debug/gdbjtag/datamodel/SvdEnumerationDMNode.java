
package com.lembed.lite.studio.debug.gdbjtag.datamodel;

import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.svd.AbstractTreePreOrderIterator;
import com.lembed.lite.studio.device.core.svd.ITreeIterator;
import com.lembed.lite.studio.device.core.svd.Leaf;
import com.lembed.lite.studio.device.core.svd.Node;

/**
 * The Class SvdEnumerationDMNode.
 */
public class SvdEnumerationDMNode extends SvdObjectDMNode {

	// ------------------------------------------------------------------------

	private String fUsage;
	private SvdEnumeratedValueDMNode fDefaultEnumerationNode;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new svd enumeration DM node.
	 *
	 * @param node the node
	 */
	public SvdEnumerationDMNode(Leaf node) {
		super(node);

		fUsage = null;
		fDefaultEnumerationNode = null;

		// If not derived from another enumeration, trigger processing for
		// <enumeratedValue> and setting for a default value.
		getChildren();
	}

	@Override
	public void dispose() {

		fUsage = null;
		if (fDefaultEnumerationNode != null) {
			fDefaultEnumerationNode.dispose();
			fDefaultEnumerationNode = null;
		}

		super.dispose();
	}

	// ------------------------------------------------------------------------

	@Override
	protected SvdObjectDMNode[] prepareChildren(Leaf node) {

		if (node == null) {
			return null;
		}

		Leaf startNode = getDerivedFromNode();
		if (startNode == null) {
			startNode = node;
		}

		if (!startNode.hasChildren()) {
			return null;
		}

		List<SvdObjectDMNode> list = new LinkedList<>();

		for (Leaf child : ((Node) startNode).getChildren()) {

			// Consider only <enumeratedValue> nodes
			if (child.isType("enumeratedValue")) { //$NON-NLS-1$

				SvdEnumeratedValueDMNode enumeratedValue = new SvdEnumeratedValueDMNode(child);

				String value = enumeratedValue.getValue();
				if (!value.isEmpty()) {
					list.add(enumeratedValue);
				} else if (enumeratedValue.isDefault()) {
					if (fDefaultEnumerationNode == null) {
						fDefaultEnumerationNode = enumeratedValue;
					} else {
						// TODO: add issues
						GdbActivator.log("duplicate isDefault enumeratedValue " + enumeratedValue.getName()); //$NON-NLS-1$
					}
				}
			}
		}

		SvdObjectDMNode[] array = list.toArray(new SvdObjectDMNode[list.size()]);

		// Preserve apparition order.
		return array;
	}

	// ------------------------------------------------------------------------

	/**
	 * Enumerate all enumerations and find the derived from node. The name is
	 * taken from the derivedFrom attribute.
	 *
	 * @return a register node, or null if not found.
	 */
	@Override
	protected Leaf findDerivedFromNode() {

		String derivedFromName = getNode().getPropertyOrNull("derivedFrom"); //$NON-NLS-1$
		final SvdDerivedFromPath path = SvdDerivedFromPath.createEnumerationPath(derivedFromName);

		if (path == null) {
			return null;
		}

		Node root = getNode().getParent();
		while (!root.isType("device")) { //$NON-NLS-1$
			root = root.getParent();
		}

		ITreeIterator peripheralNodes = new AbstractTreePreOrderIterator() {

			@Override
			public boolean isIterable(Leaf node) {

				if (node.isType("peripherals")) { //$NON-NLS-1$
					return true;
				} else if (node.isType("peripheral")) { //$NON-NLS-1$
					if (path.peripheralName == null) {
						return true;
					}
					if (path.peripheralName.equals(node.getProperty("name"))) { //$NON-NLS-1$
						return true;
					}
					return false;
				} else if (node.isType("registers")) { //$NON-NLS-1$
					return true;
				} else if (node.isType("cluster")) { //$NON-NLS-1$
					return true;
				} else if (node.isType("register")) { //$NON-NLS-1$
					if (path.registerName == null) {
						return true;
					}
					if (path.registerName.equals(node.getProperty("name"))) { //$NON-NLS-1$
						return true;
					}
					return false;
				} else if (node.isType("fields")) { //$NON-NLS-1$
					return true;
				} else if (node.isType("field")) { //$NON-NLS-1$
					if (path.fieldName == null) {
						return true;
					}
					if (path.fieldName.equals(node.getProperty("name"))) { //$NON-NLS-1$
						return true;
					}
					return false;
				} else if (node.isType("enumeratedValues")) { //$NON-NLS-1$
					if (path.enumerationName == null) {
						return true;
					}
					if (path.enumerationName.equals(node.getProperty("name"))) { //$NON-NLS-1$
						return true;
					}
					return false;
				}
				return false;
			}

			@Override
			public boolean isLeaf(Leaf node) {

				if (node.isType("enumeratedValues")) { //$NON-NLS-1$
					return true;
				}
				return false;
			}
		};

		// Iterate only the current device children nodes
		peripheralNodes.setTreeNode(root);

		Leaf ret = null;
		for (Leaf node : peripheralNodes) {

			// GdbActivator.log(node);
			if (node.isType("enumeratedValues")) { //$NON-NLS-1$
				// There should be only one, filtered by the iterator.
				if (ret == null) {
					ret = node;
				} else {
					GdbActivator.log("Non unique SVD path " + path); //$NON-NLS-1$
				}
			}
		}

		return ret;
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the default enumeration node.
	 *
	 * @return the default enumeration node
	 */
	public SvdEnumeratedValueDMNode getDefaultEnumerationNode() {
		return fDefaultEnumerationNode;
	}

	/**
	 * Gets the usage.
	 *
	 * @return the usage
	 */
	public String getUsage() {

		if (fUsage == null) {
			fUsage = getNode().getProperty("usage"); //$NON-NLS-1$
		}

		if (fUsage.isEmpty()) {
			fUsage = "read-write"; //$NON-NLS-1$
		}

		return fUsage;
	}

	/**
	 * Checks if is usage read.
	 *
	 * @return true, if is usage read
	 */
	public boolean isUsageRead() {

		return ("read".equals(getUsage()) || "read-write".equals(getUsage())); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Checks if is usage write.
	 *
	 * @return true, if is usage write
	 */
	public boolean isUsageWrite() {

		return ("write".equals(getUsage()) || "read-write".equals(getUsage())); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {

		if (getName().isEmpty()) {
			return "[" + getClass().getSimpleName() + ": \"" + getDescription() + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} 
		return "[" + getClass().getSimpleName() + ": " + getName() + ", \"" + getDescription() + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	// ------------------------------------------------------------------------
}
