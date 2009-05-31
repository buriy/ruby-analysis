package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class IfElseInstruction implements CFGNode {

	private VariableReference testVar;
	
	public IfElseInstruction(VariableReference testVar) {
		this.testVar = testVar;
	}
	
	public Set<VariableReference> uses() {
		return Collections.singleton(testVar);
	}

	public void visit(IRVisitor visitor) {
		visitor.visitIf(this);
	}

}
