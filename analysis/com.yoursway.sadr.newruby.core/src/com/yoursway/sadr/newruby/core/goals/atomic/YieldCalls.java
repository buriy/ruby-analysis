package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.InstanceMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.SingletonMethodDefinition;

public class YieldCalls extends AbstractGoal<List<Call>> {

	private final InstanceMethodDefinition def;
	private final SingletonMethodDefinition def2;

	public YieldCalls(InstanceMethodDefinition def) {
		this.def = def;
		def2 = null;
		result = new ArrayList<Call>();
	}
	
	public YieldCalls(SingletonMethodDefinition def) {
		def2 = def;
		this.def = null;
		result = new ArrayList<Call>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
