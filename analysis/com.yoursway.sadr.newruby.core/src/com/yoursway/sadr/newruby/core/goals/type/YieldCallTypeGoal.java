package com.yoursway.sadr.newruby.core.goals.type;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.EnclosingCallable;
import com.yoursway.sadr.newruby.core.goals.atomic.ReturnValues;
import com.yoursway.sadr.newruby.core.goals.other.CallersGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.YieldCall;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class YieldCallTypeGoal extends TypeGoal {

	private final YieldCall call;

	public YieldCallTypeGoal(YieldCall call) {
		this.call = call;
		result = new TypeDescription();
	}
	
	@Override
	protected void evaluate() {
		result = new TypeDescription();
		
		Callable callable = resultOf(new EnclosingCallable(call));
		if (callable == null)
			return;
		
		List<Call> callers = resultOf(new CallersGoal(callable));
		
		List<VariableReference> returned = new ArrayList<VariableReference>();
		
		for (Call c : callers) {
			RubyBlock block = c.passedBlock();
			if (block != null) {
				returned.addAll(resultOf(new ReturnValues(block)));
			}
		}
		
		for (VariableReference v : returned) {
			result.intersectWith(resultOf(TypeGoal.create(v)));
		}
	}

}
