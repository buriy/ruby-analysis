package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class GlobalAssigmentsTo extends AbstractGoal<List<Assignment>> {

	private final String name;

	public GlobalAssigmentsTo(String name) {
		this.name = name;
		result = new ArrayList<Assignment>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
