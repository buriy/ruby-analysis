package com.yoursway.sadr.newruby.core.goals.atomic;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.Call;

public class GlobalCallsWith extends AbstractGoal<List<Call>> {

	private final String name;

	public GlobalCallsWith(String name) {
		this.name = name;
		result = new ArrayList<Call>();
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
