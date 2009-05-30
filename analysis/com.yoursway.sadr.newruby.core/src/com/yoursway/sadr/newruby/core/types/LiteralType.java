package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class LiteralType implements TypeBase {


	private final Assignment a;

	public LiteralType(Assignment a) {
		this.a = a;
		if (!(a.rhs() instanceof Literal))
			throw new IllegalArgumentException();
	}

	public Assignment def() {
		return a;
	}


}
