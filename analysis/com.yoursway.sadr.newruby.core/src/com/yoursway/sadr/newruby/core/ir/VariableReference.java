package com.yoursway.sadr.newruby.core.ir;

import java.util.Collections;
import java.util.Set;

public abstract class VariableReference implements IRRHS {

	private final String name;
	
	public VariableReference(String name) {
		this.name = name;
	}
	
	public Set<VariableReference> uses() {
		return Collections.singleton(this);
	}
	
	public String name() {
		return name;
	}
	
}
