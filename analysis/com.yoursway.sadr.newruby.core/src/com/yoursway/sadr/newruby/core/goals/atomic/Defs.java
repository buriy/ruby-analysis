package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.ddp.IFileDependant;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class Defs extends AbstractGoal<List<Assignment>> implements IFileDependant {

	private final LocalVarReference var;

	public Defs(LocalVarReference var) {
		this.var = var;
		this.result = new ArrayList<Assignment>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

	public String fileName() {
		// TODO Auto-generated method stub
		return null;
	}

}
