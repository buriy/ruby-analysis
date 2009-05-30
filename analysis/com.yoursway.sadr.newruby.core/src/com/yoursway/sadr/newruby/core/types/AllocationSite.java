package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;

public class AllocationSite implements TypeBase {
	private final Assignment a;

	public AllocationSite(Assignment a) {
		this.a = a;
		if (!(a.rhs() instanceof Call))
			throw new IllegalArgumentException();
	}

	public Assignment def() {
		return a;
	}

}
