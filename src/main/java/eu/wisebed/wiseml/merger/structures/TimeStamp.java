package eu.wisebed.wiseml.merger.structures;

import eu.wisebed.wiseml.merger.enums.Unit;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class TimeStamp {
	
	private int offset;
	private Unit unit;
	private DateTime start;
	private DateTime instant;
	
	private boolean offsetDefined;
	
	public TimeStamp(int offset, Unit unit, DateTime start) {
		this.offset = offset;
		this.unit = unit;
		this.start = start;
		this.offsetDefined = true;
		computeInstant();
	}
	
	public TimeStamp(DateTime instant, Unit unit, DateTime start) {
		this.instant = instant;
		this.unit = unit;
		this.start = start;
		this.offsetDefined = false;
		computeOffset();
	}
	
	public boolean isOffsetDefined() {
		return offsetDefined;
	}

	public int getOffset() {
		return offset;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public DateTime getStart() {
		return start;
	}
		
	public DateTime getInstant() {
		return instant;
	}
	
	private void computeInstant() {
		switch (unit) {
		case seconds: 
			instant = start.plusSeconds(offset);
			break;
		case milliseconds: 
			instant = start.plusMillis(offset);
			break;
		}
	}
	
	private void computeOffset() {
		Interval interval = new Interval(start, instant);
		switch (unit) {
		case seconds: 
			offset = (int)(interval.toDurationMillis() / 1000);
			break;
		case milliseconds: 
			offset = (int)(interval.toDurationMillis());
			break;
		}
	}

	public void setStart(DateTime start) {
		this.start = start;
		computeOffset();
	}

	public void setUnit(Unit unit) {
		if (!this.unit.equals(unit)) {
			this.unit = unit;
			computeOffset();
		}
	}
	
	@Override
	public String toString() {
		if (offsetDefined) {
			return Integer.toString(offset);
		}
		return instant.toString();
	}

	public void setOffsetDefined(boolean offsetDefined) {
		this.offsetDefined = offsetDefined;
	}

}
