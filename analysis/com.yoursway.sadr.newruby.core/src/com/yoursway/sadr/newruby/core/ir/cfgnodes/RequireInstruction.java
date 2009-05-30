package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class RequireInstruction extends AbstractSingleArgumentThing implements CFGNode {

	public RequireInstruction(Literal literal) {
		super(literal);
	}

	public RequireInstruction(VariableReference var) {
		super(var);
	}

}
