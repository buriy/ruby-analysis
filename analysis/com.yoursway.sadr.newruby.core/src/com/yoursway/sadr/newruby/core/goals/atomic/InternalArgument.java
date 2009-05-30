package com.yoursway.sadr.newruby.core.goals.atomic;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class InternalArgument extends AbstractGoal<Assignment> {

	private final Call call;
	private final int argNo;
	private final Callable callable;

	public InternalArgument(Call call, int argNo, Callable callable) {
		this.call = call;
		this.argNo = argNo;
		this.callable = callable;
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
