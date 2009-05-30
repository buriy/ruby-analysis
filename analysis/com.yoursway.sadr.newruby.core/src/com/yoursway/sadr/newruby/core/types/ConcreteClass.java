package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class ConcreteClass implements TypeBase {

	private final String name;
	
	public ConcreteClass(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	public Assignment def() {
		return null;
	}
	
}
