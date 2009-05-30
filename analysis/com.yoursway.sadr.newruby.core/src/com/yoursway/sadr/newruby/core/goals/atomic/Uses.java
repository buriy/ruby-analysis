package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.ddp.IFileDependant;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class Uses extends AbstractGoal<List<CFGNode>> implements IFileDependant {

	private final Assignment def;

	public Uses(Assignment def) {
		this.def = def;
		this.result =  new ArrayList<CFGNode>();
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
