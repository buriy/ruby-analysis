package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class ReturnValues extends AbstractGoal<List<VariableReference>> {

	private final Callable callable;

	public ReturnValues(Callable callable) {
		this.callable = callable;
		result = new ArrayList<VariableReference>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
