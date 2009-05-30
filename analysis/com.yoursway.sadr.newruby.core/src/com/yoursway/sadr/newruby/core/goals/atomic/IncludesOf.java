package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IncludeInstruction;

public class IncludesOf extends AbstractGoal<List<IncludeInstruction>> {

	private final ConstantReference ref;

	public IncludesOf(ConstantReference ref) {
		this.ref = ref;
		result = new ArrayList<IncludeInstruction>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
