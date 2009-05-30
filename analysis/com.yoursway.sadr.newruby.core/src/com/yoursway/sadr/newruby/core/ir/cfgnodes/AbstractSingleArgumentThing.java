package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class AbstractSingleArgumentThing {

	private final VariableReference variable;
	private final Literal literal;
	
	public AbstractSingleArgumentThing(VariableReference var) {
		variable = var;
		literal = null;
	}
	
	public AbstractSingleArgumentThing(Literal literal) {
		this.literal = literal;
		variable = null;
	}

	public Set<VariableReference> uses() {
		if (variable() != null)
			return Collections.singleton(variable());
		return Collections.emptySet();
	}

	public VariableReference variable() {
		return variable;
	}

	public Literal literal() {
		return literal;
	}
	
}
