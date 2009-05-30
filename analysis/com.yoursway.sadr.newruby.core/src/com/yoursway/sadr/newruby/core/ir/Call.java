package com.yoursway.sadr.newruby.core.ir;

import java.util.Collection;

import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public abstract class Call implements IRRHS {

	public abstract RubyBlock passedBlock();

	public boolean hasArgument(VariableReference var) {
		return arguments().contains(var);
	}

	public abstract Collection<VariableReference> arguments();

	public Assignment parentAssignment() {
		// TODO 2B implemented by 4dman
		throw new ToBeImplementedByFourdmanException();
	}
}
