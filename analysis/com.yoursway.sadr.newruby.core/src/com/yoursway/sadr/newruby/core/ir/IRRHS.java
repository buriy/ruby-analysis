package com.yoursway.sadr.newruby.core.ir;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.cfg.UsesContainer;

public interface IRRHS extends UsesContainer {

	void visit(IRVisitor visitor);

	CFGNode enclosingInstruction();
	
}
