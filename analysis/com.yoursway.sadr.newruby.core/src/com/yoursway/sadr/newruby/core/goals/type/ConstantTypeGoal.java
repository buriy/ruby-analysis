package com.yoursway.sadr.newruby.core.goals.type;

import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.GlobalAssigmentsTo;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class ConstantTypeGoal extends TypeGoal {

	private final ConstantReference var;

	public ConstantTypeGoal(ConstantReference var) {
		this.var = var;
		result = new TypeDescription();
	}
	
	@Override
	protected void evaluate() {
		result = new TypeDescription();
		List<Assignment> defs = resultOf(new GlobalAssigmentsTo(var.name())); 
		// TODO: handle scoping

		for (Assignment a : defs) {			
			result.intersectWith(resultOf(TypeGoal.create(a.rhs())));
		}
	}
}
