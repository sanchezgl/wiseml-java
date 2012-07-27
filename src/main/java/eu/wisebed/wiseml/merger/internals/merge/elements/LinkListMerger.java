package eu.wisebed.wiseml.merger.internals.merge.elements;

import eu.wisebed.wiseml.merger.config.MergerConfiguration;
import eu.wisebed.wiseml.merger.internals.WiseMLSequence;
import eu.wisebed.wiseml.merger.internals.merge.MergerResources;
import eu.wisebed.wiseml.merger.internals.merge.SortedListMerger;
import eu.wisebed.wiseml.merger.internals.merge.WiseMLTreeMerger;
import eu.wisebed.wiseml.merger.internals.parse.elements.LinkPropertiesParser;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLTreeReader;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLTreeReaderHelper;
import eu.wisebed.wiseml.merger.internals.tree.elements.LinkPropertiesReader;
import eu.wisebed.wiseml.merger.structures.LinkProperties;

import java.util.Collection;

public class LinkListMerger extends SortedListMerger<LinkDefinition> {

	public LinkListMerger(
			final WiseMLTreeMerger parent,
			final WiseMLTreeReader[] inputs, 
			final MergerConfiguration configuration,
			final MergerResources resources) {
		super(parent, inputs, configuration, resources, WiseMLSequence.SetupLink);
	}

	@Override
	protected WiseMLTreeReader mergeItems(Collection<LinkDefinition> items) {
		LinkDefinition firstItem = null;
		String outputSource = null;
		String outputTarget = null;
		LinkProperties outputProperties = null;
		for (LinkDefinition item : items) {
			if (firstItem == null) {
				firstItem = item;
				outputSource = firstItem.getSource();
				outputTarget = firstItem.getTarget();
				outputProperties = firstItem.getLinkProperties();
			} else {
				if (!item.getLinkProperties().equals(firstItem.getLinkProperties())) {
					// conflict
					// TODO
				}
			}
		}
		return new LinkPropertiesReader(
				this, outputSource, outputTarget, outputProperties);
	}

	@Override
	protected LinkDefinition readNextItem(int inputIndex) {
		if (!nextSubInputReader(inputIndex)) {
			return null;
		}
		WiseMLTreeReader linkReader = getSubInputReader(inputIndex);
		
		// parse properties and transform
		LinkProperties properties = 
			new LinkPropertiesParser(linkReader).getParsedStructure();
		properties = resources.getLinkPropertiesTransformer().transform(
				properties, inputIndex);
		
		LinkDefinition result = new LinkDefinition(
				WiseMLTreeReaderHelper.getAttributeValue(
						linkReader.getAttributeList(), "source"),
				WiseMLTreeReaderHelper.getAttributeValue(
						linkReader.getAttributeList(), "target"),
				properties,
				inputIndex);
		return result;
	}

}
