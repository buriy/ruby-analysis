package com.yoursway.sadr.newruby.core.goals.type;

import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.ActualArgument;
import com.yoursway.sadr.newruby.core.goals.atomic.Defs;
import com.yoursway.sadr.newruby.core.goals.atomic.EnclosingCallable;
import com.yoursway.sadr.newruby.core.goals.other.CallersGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.ArgumentInitialization;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class LocalVarTypeGoal extends TypeGoal {

	private final LocalVarReference var;

	public LocalVarTypeGoal(LocalVarReference var) {
		this.var = var;
		result = new TypeDescription();
	}

	@Override
	protected void evaluate() {
		result = new TypeDescription();
		List<Assignment> defs = resultOf(new Defs(var));

		ArgumentInitialization argInit = null;
		for (Assignment a : defs) {
			if (a instanceof ArgumentInitialization) {
				if (argInit != null)
					throw new AssertionError(
							"two argument initializations for a single var");
				argInit = (ArgumentInitialization) a;
				continue;
			}
			result.intersectWith(resultOf(TypeGoal.create(a.rhs())));
		}

		if (argInit != null) {
			appendIncomingArgumentValues(argInit);
		}
	}

	private void appendIncomingArgumentValues(ArgumentInitialization argInit) {
		Callable callable = resultOf(new EnclosingCallable(argInit));
		if (callable != null) {
			List<Call> calls = resultOf(new CallersGoal(callable));
			for (Call c : calls) {
				VariableReference v = resultOf(new ActualArgument(callable, var
						.name(), c));
				result.intersectWith(resultOf(TypeGoal.create(v)));
			}
		}
	}

}
