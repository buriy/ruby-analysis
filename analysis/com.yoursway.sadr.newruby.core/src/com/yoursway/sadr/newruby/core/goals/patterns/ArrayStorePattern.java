package com.yoursway.sadr.newruby.core.goals.patterns;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class ArrayStorePattern implements INodePattern {

	public boolean matches(CFGNode node) {
		if (node instanceof MethodCall) {
			MethodCall methodCall = (MethodCall) node;
			if (methodCall.selector().equals("[]=") && methodCall.arguments().size() == 1)
				return true;
		}
		return false;
	}

	public static VariableReference storedVar(CFGNode node) {
		if (node instanceof MethodCall) {
			MethodCall methodCall = (MethodCall) node;
			if (methodCall.selector().equals("[]=")) {
				return methodCall.arguments().get(0);
			}
		}
		throw new IllegalArgumentException("array store without argument");
	}

}
