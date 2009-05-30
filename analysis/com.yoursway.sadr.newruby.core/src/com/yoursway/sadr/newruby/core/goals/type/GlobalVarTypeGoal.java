package com.yoursway.sadr.newruby.core.goals.type;

import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.GlobalAssigmentsTo;
import com.yoursway.sadr.newruby.core.ir.GlobalVarReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class GlobalVarTypeGoal extends TypeGoal {

	private final GlobalVarReference var;

	public GlobalVarTypeGoal(GlobalVarReference var) {
		this.var = var;
		result = new TypeDescription();
	}
	
	@Override
	protected void evaluate() {
		result = new TypeDescription();
		List<Assignment> defs = resultOf(new GlobalAssigmentsTo(var.name()));

		for (Assignment a : defs) {			
			result.intersectWith(resultOf(TypeGoal.create(a.rhs())));
		}
	}

}
