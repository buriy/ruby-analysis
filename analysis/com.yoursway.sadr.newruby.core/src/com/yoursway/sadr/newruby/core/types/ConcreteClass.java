package com.yoursway.sadr.newruby.core.types;


public class ConcreteClass implements TypeBase {

	private final String name;
	
	public ConcreteClass(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}
	
}
