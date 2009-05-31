package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collection;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;

public interface LocalVariablesContainter {

	Collection<Assignment> defs(LocalVarReference var);
	Collection<CFGNode> uses(Assignment ass);
	
}
