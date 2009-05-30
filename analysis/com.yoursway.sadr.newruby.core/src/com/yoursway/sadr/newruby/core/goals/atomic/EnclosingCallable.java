package com.yoursway.sadr.newruby.core.goals.atomic;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.YieldCall;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class EnclosingCallable extends AbstractGoal<Callable>{

	private final CFGNode node;
	private final VariableReference var;

	public EnclosingCallable(CFGNode node) {
		this.node = node;
		this.result = null;
	}
	
	public EnclosingCallable(VariableReference var) {
		this.var = var;
	}

	public EnclosingCallable(Callable callable) {
		// TODO Auto-generated constructor stub
	}

	public EnclosingCallable(YieldCall call) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
