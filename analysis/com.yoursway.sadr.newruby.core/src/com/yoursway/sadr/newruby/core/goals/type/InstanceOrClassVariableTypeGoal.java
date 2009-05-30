package com.yoursway.sadr.newruby.core.goals.type;

import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.GlobalAssigmentsTo;
import com.yoursway.sadr.newruby.core.ir.ClassVarReference;
import com.yoursway.sadr.newruby.core.ir.InstanceVarReference;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class InstanceOrClassVariableTypeGoal extends TypeGoal {

	private final VariableReference var;

	public InstanceOrClassVariableTypeGoal(VariableReference var) {
		this.var = var;
		if (!(var instanceof InstanceVarReference || var instanceof ClassVarReference))
			throw new IllegalArgumentException("var is not InstanceVarReference or ClassVarReference");
		result = new TypeDescription();
	}
	
	@Override
	protected void evaluate() {
		result = new TypeDescription();
		List<Assignment> ass = resultOf(new GlobalAssigmentsTo(var.name()));
		TypeDescription selfType = resultOf(new SelfTypeGoal(var));
		for (Assignment a : ass) {
			TypeDescription anotherSelf = resultOf(new SelfTypeGoal(a));
			if (selfType.hasNonEmptyIntersectionWith(anotherSelf))
				result.intersectWith(resultOf(TypeGoal.create(a.rhs())));
		}
	}

}
