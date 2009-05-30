package com.yoursway.sadr.newruby.core.goals.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.goals.atomic.ReturnValues;
import com.yoursway.sadr.newruby.core.goals.other.RespondersGoal;
import com.yoursway.sadr.newruby.core.goals.other.UsagesGoal;
import com.yoursway.sadr.newruby.core.goals.patterns.ArrayStorePattern;
import com.yoursway.sadr.newruby.core.goals.patterns.HashStorePattern;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
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
		List<Assignment> defs = receiverType.defs();
		
		List<VariableReference> vals = new ArrayList<VariableReference>();
		if (receiverType.canBeArray() && call.selector().equals("[]")) {
			for (Assignment a : defs) {
				Collection<CFGNode> uses = resultOf(new UsagesGoal(a, new ArrayStorePattern()));
				for (CFGNode n : uses) {
					vals.add(ArrayStorePattern.storedVar(n));
				}
			}
		} 
		
		if (receiverType.canBeHash() && call.selector().equals("[]") && call.hasSingleLiteralArgument()) {
			for (Assignment a : defs) {
				Collection<CFGNode> uses = resultOf(new UsagesGoal(a, new HashStorePattern()));
				for (CFGNode n : uses) {
					vals.add(HashStorePattern.storedVar(n));
				}
			}
		}
		
		for (VariableReference v : vals) 
			result.intersectWith(resultOf(TypeGoal.create(v)));
	}

}
