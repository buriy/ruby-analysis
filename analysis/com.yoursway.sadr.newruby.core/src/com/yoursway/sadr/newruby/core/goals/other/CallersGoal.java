package com.yoursway.sadr.newruby.core.goals.other;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class CallersGoal extends AbstractGoal<List<Call>> {

	private final Callable callable;

	public CallersGoal(Callable callable) {
		this.callable = callable;
		result = new ArrayList<Call>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
