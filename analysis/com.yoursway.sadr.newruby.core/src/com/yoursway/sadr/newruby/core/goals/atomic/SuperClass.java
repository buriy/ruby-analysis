package com.yoursway.sadr.newruby.core.goals.atomic;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class SuperClass extends AbstractGoal<Assignment> {

	private final Assignment ass;

	public SuperClass(Assignment ass) {
		this.ass = ass;
		result = null; // = Magic.objectClassDeclaration()
	}
	
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub
		
	}

}
