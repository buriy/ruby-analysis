package com.yoursway.sadr.newruby.core.goals.atomic;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.MethodDeclaration;

public class EnclosingMethod extends AbstractGoal<MethodDeclaration> {

	private final CFGNode node;

	public EnclosingMethod(CFGNode node) {
		this.node = node;
		result = null;
	}
	
	public EnclosingMethod(VariableReference var) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void evaluate() {
		
	}

}
