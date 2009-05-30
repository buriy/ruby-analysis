package com.yoursway.sadr.newruby.core.goals.atomic;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class ActualArgument extends AbstractGoal<VariableReference> {

	private final Callable callable;
	private final String argName;
	private final Call call;

	public ActualArgument(Callable callable, String argName, Call call) {
		this.callable = callable;
		this.argName = argName;
		this.call = call;
		result = null;
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
