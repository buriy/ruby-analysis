package com.yoursway.sadr.newruby.core.goals.type;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.newruby.core.goals.atomic.ReturnValues;
import com.yoursway.sadr.newruby.core.goals.other.RespondersGoal;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class MethodCallTypeGoal extends TypeGoal {

	private final MethodCall call;

	public MethodCallTypeGoal(MethodCall call) {
		this.call = call;
		result = new TypeDescription();
	}

	@Override
	protected void evaluate() {
		result = new TypeDescription();
		List<Callable> responders = resultOf(new RespondersGoal(call));
		handleCollections();
		List<VariableReference> returned = new ArrayList<VariableReference>();
		for (Callable c : responders) {
			returned.addAll(resultOf(new ReturnValues(c)));
		}
		for (VariableReference v : returned) {
			result.intersectWith(resultOf(TypeGoal.create(v)));
		}
	}

	private void handleCollections() {
		TypeDescription receiverType = resultOf(TypeGoal.create(call.receiver()));
		
		if (receiverType.canBeArray() && call.selector().equals("[]")) {
			
		} 
		
		if (receiverType.canBeHash() && call.selector().equals("[]") && call.hasSingleLiteralArgument()) {
			
		}
	}

}
