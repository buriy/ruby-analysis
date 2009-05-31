package com.yoursway.sadr.newruby.core.ir;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class YieldCall extends Call {

	public Set<VariableReference> uses() {
		return Collections.emptySet();
	}

	public void visit(IRVisitor visitor) {
		visitor.visitYieldCall(this);
	}

}
