package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class BreakInstruction implements CFGNode {

	public Set<VariableReference> uses() {
		return Collections.emptySet();
	}

}
