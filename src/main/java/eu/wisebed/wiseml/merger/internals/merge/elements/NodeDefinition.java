package eu.wisebed.wiseml.merger.internals.merge.elements;

import eu.wisebed.wiseml.merger.structures.NodeProperties;

public class NodeDefinition implements Comparable<NodeDefinition> {
	
	private final String id;
	private final NodeProperties nodeProperties;
	private final int inputIndex;
	
	public NodeDefinition(
			final String id, 
			final NodeProperties nodeProperties, int inputIndex) {
		this.id = id;
		this.nodeProperties = nodeProperties;
		this.inputIndex = inputIndex;
	}

	@Override
	public int compareTo(NodeDefinition other) {
		return this.id.compareTo(other.id);
	}

	public String getId() {
		return id;
	}

	public NodeProperties getNodeProperties() {
		return nodeProperties;
	}

	public int getInputIndex() {
		return inputIndex;
	}
	
	@Override
	public String toString() {
		return inputIndex+"#"+id;
	}

}
