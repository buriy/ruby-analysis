package com.yoursway.sadr.newruby.core.ir;

import java.util.List;

import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class MethodArguments {

	private final List<NamedArgument> namedArgs;
	private final VarArgument varArg;
	private final BlockArgument blockArg;
	private final List<OptionalArgument> optionalArgs;
	
	public MethodArguments(List<NamedArgument> namedArgs, List<OptionalArgument> optionalArgs, VarArgument varArg,
			BlockArgument blockArg) {
		this.namedArgs = namedArgs;
		this.optionalArgs = optionalArgs;
		this.varArg = varArg;
		this.blockArg = blockArg;
	}

	public List<NamedArgument> namedArgs() {
		return namedArgs;
	}

	public VarArgument varArg() {
		return varArg;
	}

	public BlockArgument blockArg() {
		return blockArg;
	}
		
	public List<OptionalArgument> optionalArgs() {
		return optionalArgs;
	}
	
}
