package com.yoursway.sadr.newruby.core.types;

import java.util.ArrayList;
import java.util.List;

public class Type {

	private final TypeBase base;
	private final List<MethodDescription> methods = new ArrayList<MethodDescription>();

	public Type(TypeBase base) {
		this.base = base;
	}

	public TypeBase base() {
		return base;
	}

	public List<MethodDescription> methods() {
		return methods;
	}

}
