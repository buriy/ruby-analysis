package com.yoursway.sadr.newruby.core.ir;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.util.Graph;

public class OptionalArgument extends NamedArgument {

	private final Graph<CFGNode> init;
	private final VariableReference resVar;
	
	public OptionalArgument(String name, Graph<CFGNode> init, VariableReference resVar) {
		super(name);
		this.init = init;
		this.resVar = resVar;
	}

}
