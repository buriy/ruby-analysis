package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class ReturnInstruction extends AbstractSingleArgumentThing implements CFGNode {

	public ReturnInstruction(Literal literal) {
		super(literal);
	}

	public ReturnInstruction(VariableReference var) {
		super(var);
	}

	public void visit(IRVisitor visitor) {
		visitor.visitReturn(this);
	}

	
}
