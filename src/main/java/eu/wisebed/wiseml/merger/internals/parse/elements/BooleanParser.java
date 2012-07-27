package eu.wisebed.wiseml.merger.internals.parse.elements;

import eu.wisebed.wiseml.merger.internals.parse.WiseMLElementParser;
import eu.wisebed.wiseml.merger.internals.tree.WiseMLTreeReader;

public class BooleanParser extends WiseMLElementParser<Boolean> {

	public BooleanParser(WiseMLTreeReader reader) {
		super(reader);
	}

	@Override
	protected void parseStructure() {
		String value = reader.getText().toLowerCase();
		if (value.equals("true") || value.equals("false")) {
			structure = Boolean.valueOf(value);
			return;
		}
		try {
			Integer intValue = Integer.parseInt(value);
			structure = Boolean.valueOf(intValue != 0);
			return;
		} catch (NumberFormatException e) {
			// not an integer
		}
		if (value.equals("yes")) {
			structure = Boolean.TRUE;
			return;
		}
		if (value.equals("no")) {
			structure = Boolean.FALSE;
			return;
		}
		reader.exception("could not parse boolean value: "+value, null);
	}

}
