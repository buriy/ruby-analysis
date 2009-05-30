package com.yoursway.sadr.newruby.core.ir;

public class ConstantReference extends VariableReference {

	public static ConstantReference resolved(String fqn) {
		return new ConstantReference(fqn, fqn, null);
	}
	
	public static ConstantReference unresolved(String name, String fqnPrefix) {
		return new ConstantReference(name, null, fqnPrefix);
	}
	
	private String fqn;
	private String fqnPrefix;
	
	private ConstantReference(String name, String fqn, String fqnPrefix) {
		super(name);
		this.fqn = fqn;
		this.fqnPrefix = fqnPrefix;
	}

}
