package com.yoursway.sadr.newruby.core.goals.type;

import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.types.LiteralType;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class LiteralTypeGoal extends TypeGoal {

	private final Literal literal;

	public LiteralTypeGoal(Literal literal) {
		this.literal = literal;
	}
	
	@Override
	protected void evaluate() {
		result = new TypeDescription(new LiteralType(literal));
	}

}
