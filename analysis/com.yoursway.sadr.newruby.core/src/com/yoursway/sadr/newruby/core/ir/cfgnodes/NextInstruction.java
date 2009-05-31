package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class NextInstruction implements CFGNode {

	public Set<VariableReference> uses() {
		return Collections.emptySet();
	}

	public void visit(IRVisitor visitor) {
		visitor.visitNext(this);
	}

}
