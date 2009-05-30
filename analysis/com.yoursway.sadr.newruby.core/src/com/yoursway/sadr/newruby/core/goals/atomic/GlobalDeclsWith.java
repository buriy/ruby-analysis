package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;

public class GlobalDeclsWith extends AbstractGoal<List<Callable>> {

	private final String name;

	public GlobalDeclsWith(String name) {
		this.name = name;
		result = new ArrayList<Callable>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
