package com.yoursway.sadr.newruby.core.cfg;

import java.util.Set;

import com.yoursway.sadr.newruby.core.ir.VariableReference;

public interface UsesContainer {

	Set<VariableReference> uses();
	
}
