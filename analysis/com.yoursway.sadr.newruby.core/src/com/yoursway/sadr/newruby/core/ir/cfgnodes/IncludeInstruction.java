package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class IncludeInstruction extends AbstractSingleArgumentThing implements
		CFGNode {

	public IncludeInstruction(Literal literal) {
		super(literal);
	}

	public IncludeInstruction(VariableReference var) {
		super(var);
	}

	public ConstantReference extendedClass() {
		return null; // TODO
	}
	
}
