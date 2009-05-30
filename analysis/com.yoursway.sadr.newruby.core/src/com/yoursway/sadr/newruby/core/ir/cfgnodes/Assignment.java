package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.IRRHS;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class Assignment implements CFGNode {
	
	private final VariableReference lhs;
	private final IRRHS rhs;

	public Assignment(VariableReference lhs, IRRHS rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public IRRHS rhs() {
		return rhs;
	}

	public Set<VariableReference> uses() {
		return rhs.uses();
	}

	public VariableReference lhs() {
		return lhs;
	}

}
