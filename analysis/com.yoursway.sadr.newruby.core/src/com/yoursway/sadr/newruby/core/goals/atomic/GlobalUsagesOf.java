package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;

public class GlobalUsagesOf extends AbstractGoal<List<CFGNode>> {

	private final String name;

	public GlobalUsagesOf(String name) {
		this.name = name;
		result = new ArrayList<CFGNode>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
