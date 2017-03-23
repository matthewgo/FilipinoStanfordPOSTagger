package brown_cluster;

import java.util.List;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 */
public class ClusterHistoryNode {

	private ClusterHistoryNode parent;
	private ClusterHistoryNode leftChild;
	private ClusterHistoryNode rightChild;
	private int cluster;

	public ClusterHistoryNode(int cluster) {
		this.cluster = cluster;
	}

	public ClusterHistoryNode getParent() {
		return parent;
	}

	public void setParent(ClusterHistoryNode parent) {
		this.parent = parent;
	}

	public ClusterHistoryNode getLeftChild() {
		return leftChild;
	}

	public ClusterHistoryNode getRightChild() {
		return rightChild;
	}

	public void setChildren(ClusterHistoryNode node1, ClusterHistoryNode node2) {
		node1.setParent(this);
		node2.setParent(this);
		leftChild = node1;
		rightChild = node2;
	}

	public int getCluster() {
		return cluster;
	}

	public boolean isLeafNode() {
		return leftChild == null;
	}

	public void getAllLeafNodes(List<ClusterHistoryNode> result) {
		if (isLeafNode()) {
			result.add(this);
		} else {
			leftChild.getAllLeafNodes(result);
			rightChild.getAllLeafNodes(result);
		}
	}
}
