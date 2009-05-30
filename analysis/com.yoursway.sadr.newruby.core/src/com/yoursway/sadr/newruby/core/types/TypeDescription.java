package com.yoursway.sadr.newruby.core.types;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class TypeDescription {

	private final Set<Type> types = new HashSet<Type>();
	
	public TypeDescription() {		
	}
	
	public TypeDescription(TypeBase tb) {
		addType(tb);
	}
	
	public void addType(Type t) {
		types.add(t);
	}
	
	public void addType(TypeBase tb) {		
		types.add(new Type(tb));
	}
	
	public void intersectWith(TypeDescription desc) {
		types.addAll(desc.types);
	}

	public boolean hasNonEmptyIntersectionWith(TypeDescription anotherSelf) {
		// TODO
		return false;
	}

	public boolean canBeArray() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean canBeHash() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Assignment> defs() {
		// TODO
		return null;
	}

}
