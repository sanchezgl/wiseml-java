package eu.wisebed.wiseml.merger.internals.merge.elements;

import eu.wisebed.wiseml.merger.config.MergerConfiguration;
import eu.wisebed.wiseml.merger.internals.WiseMLSequence;
import eu.wisebed.wiseml.merger.internals.WiseMLTag;
import eu.wisebed.wiseml.merger.internals.merge.MergerResources;
import eu.wisebed.wiseml.merger.internals.merge.SortedListMerger;
import eu.wisebed.wiseml.merger.internals.merge.WiseMLTreeMerger;
import eu.wisebed.wiseml.merger.internals.parse.elements.TimeStampParser;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLTreeReader;
import eu.wisebed.wiseml.merger.internals.tree.elements.TimeStampReader;
import eu.wisebed.wiseml.merger.structures.TimeInfo;
import eu.wisebed.wiseml.merger.structures.TimeStamp;

import java.util.Collection;

public abstract class TimeStampedItemListMerger 
extends SortedListMerger<TimeStampedItemDefinition> {
	
	private Collection<TimeStampedItemDefinition> items;

	public TimeStampedItemListMerger(
			final WiseMLTreeMerger parent,
			final WiseMLTreeReader[] inputs, 
			final MergerConfiguration configuration,
			final MergerResources resources,
			final WiseMLSequence sequence) {
		super(parent, inputs, configuration, resources, sequence);
	}

	@Override
	protected TimeStampedItemDefinition readNextItem(int inputIndex) {
		if (!nextSubInputReader(inputIndex)) {
			return null;
		}
		WiseMLTreeReader timestampReader = getSubInputReader(inputIndex);
		
		if (!WiseMLTag.timestamp.equals(timestampReader.getTag())) {
			throw new IllegalStateException(
					"expected <timestamp>, got <"+timestampReader.getTag()+">");
		}
		
		if (resources.getOutputTimeInfo() == null) {
			exception("encountered timestamp, but missing timeinfo", null);
		}
		
		TimeStamp timestamp = new TimeStampParser(
				timestampReader,
				resources.getInputTimeInfo(inputIndex)).getParsedStructure();
		
		TimeInfo ti = resources.getOutputTimeInfo();
		timestamp.setStart(ti.getStart());
		timestamp.setUnit(ti.getUnit());
		timestamp.setOffsetDefined(resources.isTimestampOffsetDefined());
		
		return new TimeStampedItemDefinition(timestamp, inputIndex);
	}
	
	@Override
	protected void fillQueue() {
		if (this.items == null) {
			super.fillQueue();
		} else {
			for (TimeStampedItemDefinition item : this.items) {
				int input = item.getInputIndex();
				if (nextSubInputReader(input)) {
					WiseMLTreeReader reader = getSubInputReader(input);
					
					if (reader.getTag().equals(WiseMLTag.timestamp)) {
						holdInput(item.getInputIndex());
					} else {
						handleReader(reader, input);
					}
				}
			}
			if (this.queue.isEmpty()) {
				this.items = null;
				super.fillQueue();
			}
		}
	}
	
	protected abstract void handleReader(WiseMLTreeReader reader, int inputIndex);

	@Override
	protected WiseMLTreeReader mergeItems(
			Collection<TimeStampedItemDefinition> items) {
		// save items
		this.items = items;
		
		TimeStampedItemDefinition item = items.iterator().next();
		TimeStamp timestamp = item.getTimeStamp();
		
		this.queue.add(new TimeStampReader(this, timestamp));
		
		/* nothing to return because data needs to be streamed into the 
		 * same list (managed by this merger)
		 */
		return null;
	}

}
