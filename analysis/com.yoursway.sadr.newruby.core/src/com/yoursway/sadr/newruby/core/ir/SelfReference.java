package com.yoursway.sadr.newruby.core.ir;

import com.yoursway.sadr.newruby.core.valuesandtypes.ValueSet;

public class SelfReference extends VariableReference {

	private ValueSet selfOverride;

	public SelfReference() {
		super("self");
	}

	public void setValue(ValueSet selfOverride) {
		this.selfOverride = selfOverride;
		
	}

}
