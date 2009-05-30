package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class InstanceOfContreteClass implements TypeBase {

	private final ConcreteClass klass;
	
	public InstanceOfContreteClass(ConcreteClass klass) {
		this.klass = klass;
	}

	public String name() {
		return klass().name();
	}

	public ConcreteClass klass() {
		return klass;
	}

	public Assignment def() {
		return null;
	}
	
}
