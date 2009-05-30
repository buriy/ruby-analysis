package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import com.yoursway.sadr.newruby.core.ir.IRRHS;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class ArgumentInitialization  extends Assignment {

	public ArgumentInitialization(VariableReference lhs, IRRHS rhs) {
		super(lhs, rhs);
	}

}
