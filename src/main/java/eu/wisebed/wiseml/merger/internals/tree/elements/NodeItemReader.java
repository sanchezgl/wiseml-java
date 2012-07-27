package eu.wisebed.wiseml.merger.internals.tree.elements;

import eu.wisebed.wiseml.merger.internals.WiseMLAttribute;
import eu.wisebed.wiseml.merger.internals.WiseMLTag;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLStructureReader;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLTreeReader;
import eu.wisebed.wiseml.merger.structures.NodeItem;

public class NodeItemReader extends WiseMLStructureReader {

	public NodeItemReader(WiseMLTreeReader parent, NodeItem nodeItem) {
		super(new Element(
				parent, 
				WiseMLTag.node, 
				new WiseMLAttribute[]{
					new WiseMLAttribute("id", nodeItem.getId())
				},
				createSubElements(nodeItem), null));
	}

	private static Element[] createSubElements(NodeItem nodeItem) {
		int offset = nodeItem.getPosition() != null ? 1 : 0;
		Element[] result = new Element[nodeItem.dataItemCount() + offset];
		
		if (offset > 0) {
			CoordinateReader coordinateReader = new CoordinateReader(
					null, WiseMLTag.position, nodeItem.getPosition());
			result[0] = coordinateReader.getTopElement();
		}
		
		for (int i = 0; i < result.length - offset; i++) {
			result[offset + i] = new Element(
					null,
					WiseMLTag.data,
					new WiseMLAttribute[]{
						new WiseMLAttribute("key", nodeItem.getKey(i)),
					},
					null,
					nodeItem.getData(i));
		}
		
		return result;
	}

}
